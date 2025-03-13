package com.snapbizz.core.logger

import android.content.Context
import com.snapbizz.core.database.dao.LogDao
import com.snapbizz.core.database.entities.LogEntity
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton


@EntryPoint
@InstallIn(SingletonComponent::class)
interface SnapLoggerEntryPoint {
    fun getSnapLogger(): SnapLogger
}

@Singleton
class SnapLogger @Inject constructor(
    private val logDao: LogDao, @ApplicationContext private val context: Context
) {
    fun log(priority: PRIORITY, tag: String, source: String, message: String) {
        Timber.tag(tag).d(message)
        when (priority) {
            PRIORITY.HIGH -> saveToDatabase(tag, source, message)
            PRIORITY.LOW -> logToFile(tag, source, message)
        }
    }

    private fun logToFile(tag: String, source: String, message: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        val logFileName = "${currentDate}_log.txt"
        val logsDir = File(context.filesDir, "Logs")
        if (!logsDir.exists()) {
            logsDir.mkdirs()
        }
        val logFile = File(logsDir, logFileName)

        try {
            FileWriter(logFile, true).use { writer ->
                writer.append("${System.currentTimeMillis()} | [$tag] ($source): $message\n")
                writer.flush()
            }
        } catch (e: IOException) {
            Timber.e("Failed to write log to file: ${e.localizedMessage}")
        }
    }

    private fun saveToDatabase(tag: String, source: String, message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            logDao.insertLog(
                LogEntity(
                    tag = tag,
                    source = source,
                    message = message,
                    timestamp = System.currentTimeMillis().toString()
                )
            )
        }
    }

    companion object {
        fun log() {
            TODO("Not yet implemented")
        }
    }
}

enum class PRIORITY {
    HIGH, LOW
}