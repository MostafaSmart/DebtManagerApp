package com.smarttec.moveisapptow.data.db.database

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class DataBaseBackApp {


    fun backupDatabase(context: Context): String? {
        return try {
            val dbPath = context.getDatabasePath("user_database").absolutePath
            val backupPath = File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                "user_database_backup.db"
            )

            val src = FileInputStream(dbPath)
            val dest = FileOutputStream(backupPath)

            src.channel.use { inputChannel ->
                dest.channel.use { outputChannel ->
                    outputChannel.transferFrom(inputChannel, 0, inputChannel.size())
                }
            }

            src.close()
            dest.close()

            backupPath.absolutePath // Return the backup file path
        } catch (e: Exception) {
            e.printStackTrace()
            null // Return null if backup fails
        }
    }



    fun restoreDatabase(context: Context, backupFilePath: String): Boolean {
        return try {
            val dbPath = context.getDatabasePath("user_database").absolutePath
            val backupFile = File(backupFilePath)

            if (backupFile.exists()) {
                val src = FileInputStream(backupFile)
                val dest = FileOutputStream(dbPath)

                src.channel.use { inputChannel ->
                    dest.channel.use { outputChannel ->
                        outputChannel.transferFrom(inputChannel, 0, inputChannel.size())
                    }
                }

                src.close()
                dest.close()

                true // Restore successful
            } else {
                false // Backup file not found
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false // Restore failed
        }
    }

}