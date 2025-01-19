package com.smarttec.moveisapptow.ui.activtys

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.smarttec.moveisapptow.R
import com.smarttec.moveisapptow.data.db.AppDatabase
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class BackupActivity : AppCompatActivity() {
    private val REQUEST_CODE_PICK_BACKUP = 1001

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            restoreDatabaseFromUri(this, it)
        } ?: run {
            Toast.makeText(this@BackupActivity, "لم يتم اختيار أي ملف!", Toast.LENGTH_SHORT).show()
        }
    }
    private lateinit var btnMekeBackUp: Button
    private lateinit var btnRestartBackUp: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_backup)

        btnMekeBackUp = findViewById(R.id.btnMekeBackUp)
        btnRestartBackUp = findViewById(R.id.btnRestartBackUp)


        btnMekeBackUp.setOnClickListener {
            if (hasStoragePermission()) {
                closeDatabase(this)
                val isBackupSuccessful = backupDatabaseToDocuments(this)
                if (isBackupSuccessful) {
                    Toast.makeText(this, "تم إنشاء النسخة الاحتياطية بنجاح في Documents/AppBackUp!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "فشل في إنشاء النسخة الاحتياطية!", Toast.LENGTH_SHORT).show()
                }

            } else {
                requestStoragePermission()
            }
        }

        // زر استعادة النسخة الاحتياطية
        btnRestartBackUp.setOnClickListener {
            closeDatabase(this)
            if (hasStoragePermission()) {
                filePickerLauncher.launch("*/*")
            } else {
                requestStoragePermission()
            }
        }


    }
    fun closeDatabase(context: Context) {
        AppDatabase.getInstance(context).close()
    }

    fun backupDatabaseToDocuments(context: Context): Boolean {
        return try {
            // مسار قاعدة البيانات
            val dbPath = context.getDatabasePath("user_database")

            // تحديد مسار النسخة الاحتياطية
            val backupDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "AppBackUp")
            if (!backupDir.exists()) {
                backupDir.mkdirs() // إنشاء المجلد إذا لم يكن موجودًا
            }

            val backupFile = File(backupDir, "backup.db")

            // نسخ قاعدة البيانات إلى النسخة الاحتياطية
            dbPath.copyTo(backupFile, overwrite = true)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun restoreDatabaseFromUri(context: Context, uri: Uri): Boolean {
        return try {
            // مسار قاعدة البيانات
            val dbPath = context.getDatabasePath("user_database")

            // قراءة محتوى الملف المحدد
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                dbPath.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            Toast.makeText(context, "تم استعادة النسخة الاحتياطية بنجاح!", Toast.LENGTH_SHORT).show()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "فشل في استعادة النسخة الاحتياطية!", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun hasStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // في Android 11 وما فوق، لا حاجة للإذن لأننا نستخدم Scoped Storage
            true
        } else {
            // في الإصدارات السابقة، نحتاج للتحقق من إذن الكتابة في التخزين
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }


    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
    }

//


    // التعامل مع إذن الكتابة في التخزين بعد طلبه
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "تم منح الإذن بنجاح", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "الإذن مرفوض", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

// دالة للحصول على مسار الملف من URI
//private fun getFilePathFromUri(uri: Uri): String {
//    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//    val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
//    cursor?.moveToFirst()
//    val columnIndex = cursor?.getColumnIndex(filePathColumn[0]) ?: -1
//    return cursor?.getString(columnIndex) ?: ""
//}

// فتح نافذة لاختيار ملف النسخة الاحتياطية
//    private fun openFilePicker() {
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//        intent.type = "application/*" // أو يمكنك تحديد نوع الملفات الذي تريده
//        startActivityForResult(intent, REQUEST_CODE_PICK_BACKUP)
//    }
//    // التعامل مع النتيجة بعد اختيار الملف
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CODE_PICK_BACKUP && resultCode == Activity.RESULT_OK) {
//            val uri = data?.data
//            uri?.let {
//                val file = File(getFilePathFromUri(it))
//                restoreDatabase(applicationContext, file)
//            }
//        }
//    }


//    fun backupDatabase(context: Context, backupFile: File): Boolean {
//        return try {
//            val dbPath = context.getDatabasePath("user_database") // اسم قاعدة البيانات
//            dbPath.copyTo(backupFile, overwrite = true)
//            true
//        } catch (e: Exception) {
//            e.printStackTrace()
//            false
//        }
//    }
//
//
//    private fun backupDatabase(context: Context) {
//        val currentDB = context.getDatabasePath("user_database") // مسار قاعدة البيانات
//        val backupDB = File(context.getExternalFilesDir(null), "backup_user_database") // مسار النسخة الاحتياطية
//
//        try {
//            val src = FileInputStream(currentDB).channel
//            val dst = FileOutputStream(backupDB).channel
//            dst.transferFrom(src, 0, src.size())
//            src.close()
//            dst.close()
//            Toast.makeText(context, "تم إنشاء النسخة الاحتياطية بنجاح", Toast.LENGTH_SHORT).show()
//        } catch (e: IOException) {
//            e.printStackTrace()
//            Toast.makeText(context, "حدث خطأ أثناء إنشاء النسخة الاحتياطية", Toast.LENGTH_SHORT).show()
//        }
//    }


//    // استعادة النسخة الاحتياطية
//    private fun restoreDatabase(context: Context, backupFile: File) {
//        val currentDB = context.getDatabasePath("user_database")
//        try {
//            val src = FileInputStream(backupFile).channel
//            val dst = FileOutputStream(currentDB).channel
//            dst.transferFrom(src, 0, src.size())
//            src.close()
//            dst.close()
//            Toast.makeText(context, "تم استعادة النسخة الاحتياطية بنجاح", Toast.LENGTH_SHORT).show()
//        } catch (e: IOException) {
//            e.printStackTrace()
//            Toast.makeText(context, "حدث خطأ أثناء استعادة النسخة الاحتياطية", Toast.LENGTH_SHORT).show()
//        }
//    }


//
//import android.Manifest
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.provider.MediaStore
//import android.widget.Button
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import java.io.File
//import java.io.FileInputStream
//import java.io.FileOutputStream
//import java.io.IOException
//import java.nio.channels.FileChannel
//
//class BackupActivity : AppCompatActivity() {
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_backup)
//
//        val buttonBackup = findViewById<Button>(R.id.buttonBackup)
//        val buttonRestore = findViewById<Button>(R.id.buttonRestore)
//
//        // زر إنشاء النسخة الاحتياطية
//

//    }
//
////    // التحقق من وجود إذن الكتابة في التخزين
////    private fun hasStoragePermission(): Boolean {
////        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
////            // في Android 11 وما فوق، لا حاجة للإذن لأننا نستخدم Scoped Storage
////            true
////        } else {
////            // في الإصدارات السابقة، نحتاج للتحقق من إذن الكتابة في التخزين
////            ContextCompat.checkSelfPermission(
////                this,
////                Manifest.permission.WRITE_EXTERNAL_STORAGE
////            ) == PackageManager.PERMISSION_GRANTED
////        }
////    }
//
//    // طلب إذن الكتابة في التخزين
//
//
//
//
//
//
//}
