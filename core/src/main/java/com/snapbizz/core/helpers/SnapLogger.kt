package com.snapbizz.core.helpers

import android.content.Context
import com.snapbizz.core.database.dao.LogDao
import com.snapbizz.core.database.entities.LogEntity
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object SnapLogger {

    private lateinit var logDao: LogDao
    private lateinit var appContext: Context
    private val logScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun init(context: Context, dao: LogDao) {
        appContext = context.applicationContext
        logDao = dao
        Timber.plant(Timber.DebugTree())
    }

    fun log(tag: String, message: String, module: LogModule, priority: LogPriority = LogPriority.LOW) {
        logScope.launch { logMessage(module.name, tag, message, priority) }
    }

    fun logException(tag: String, module: LogModule, exception: Exception) {
        logScope.launch {
            val stackTraceElement = exception.stackTrace.firstOrNull()
            val methodName = stackTraceElement?.methodName ?: "UnknownMethod"
            val lineNumber = stackTraceElement?.lineNumber ?: -1
            val errorMessage = exception.localizedMessage ?: "Unknown Exception"
            val logMessage = "$methodName():$lineNumber | $errorMessage"

            Timber.tag(tag).e(exception, logMessage)
            logMessage(module.name, tag, logMessage, LogPriority.LOW)
        }
    }

    private suspend fun logMessage(source: String, tag: String, message: String, priority: LogPriority, dispatcher: CoroutineDispatcher=Dispatchers.IO) {
        withContext(dispatcher) {
            Timber.tag("$source - $tag").d(message)
            when (priority) {
                LogPriority.HIGH -> saveToDatabase(tag, source, message)
                LogPriority.LOW -> logToFile(tag, source, message)
            }
        }
    }

    private suspend fun logToFile(tag: String, source: String, message: String,dispatcher: CoroutineDispatcher=Dispatchers.IO) {
        withContext(dispatcher) {
            var writer: FileWriter? = null
            try {
                writer = FileWriter(getLogFile(), true)
                writer.append("${System.currentTimeMillis()} | [$tag] ($source): $message\n")
                writer.flush()
            } catch (e: IOException) {
                Timber.e("Failed to write log to file: ${e.localizedMessage}")
            } finally {
                writer?.close()
            }
        }
    }

    private suspend fun saveToDatabase(tag: String, source: String, message: String,dispatcher: CoroutineDispatcher=Dispatchers.IO) {
        withContext(dispatcher) {
            try {
                logDao.insertLog(
                    LogEntity(
                        tag = tag,
                        source = source,
                        message = message,
                        timestamp = System.currentTimeMillis().toString()
                    )
                )
            } catch (e: Exception) {
                Timber.e("Database log insert failed: ${e.localizedMessage}")
            }
        }
    }

    private fun getLogFile(): File {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val logFileName = "${dateFormat.format(Date())}_log.txt"
        val logsDir = File(appContext.filesDir, "Logs").apply { if (!exists()) mkdirs() }
        return File(logsDir, logFileName)
    }
}



enum class LogPriority { HIGH, LOW }

enum class LogModule { HOME,  }
