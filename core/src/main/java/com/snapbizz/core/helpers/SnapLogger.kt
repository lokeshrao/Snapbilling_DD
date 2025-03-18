package com.snapbizz.core.helpers

import android.content.Context
import com.snapbizz.core.database.dao.LogDao
import com.snapbizz.core.database.entities.LogEntity
import com.snapbizz.core.utils.formatDateToString
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
            val logBuilder = StringBuilder()
            var currentException: Throwable? = exception

            while (currentException != null) {
                val stackTraceElement = currentException.stackTrace.firstOrNull()
                val methodName = stackTraceElement?.methodName ?: "UnknownMethod"
                val lineNumber = stackTraceElement?.lineNumber ?: -1
                val errorMessage = currentException.localizedMessage ?: "Unknown Exception"
                logBuilder.append("$methodName():$lineNumber | $errorMessage\n")
                currentException = currentException.cause
            }
            val finalLogMessage = logBuilder.toString().trim()
            logMessage(module.name, tag, finalLogMessage, LogPriority.LOW)
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
                writer.append("${formatDateToString(Date(),isTime = true)} | [$source] ($tag): $message\n")
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
                        timestamp = formatDateToString(Date(),isTime = true)
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
