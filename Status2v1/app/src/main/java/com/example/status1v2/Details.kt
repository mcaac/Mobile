package com.example.status1v2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import java.io.IOException

@ExperimentalStdlibApi
class Details : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val fileItem =
            intent.getParcelableExtra<FileItem>(MainActivity.MAIN_ACTIVITY_EXTRA_DATA_ID)
        val position = intent.getIntExtra(MainActivity.MAIN_ACTIVITY_EXTRA_POSITION_ID, 0)

        val arm = intent.getIntExtra(MainActivity.MAIN_ACTIVITY_EXTRA_ARM_ID, 0)

        val fileTitle: TextView = findViewById(R.id.file_name)
        val fileDescription: TextView = findViewById(R.id.file_data)
        fileTitle.text= fileItem?.FileName
        var details = ""
        if(arm.toString()=="I"){
            val file = File(filesDir, fileItem?.FileName)

            try {
                details=readEncryptedFile(file)
            }catch (e: IOException){
                try {
                    val inputStream = FileInputStream(file)
                    details = inputStream.bufferedReader().use { it.readText() }
                }catch (e: IOException){
                    throw Exception(e)
                }
            }
        }else{
            val file = File(filesDir, fileItem?.FileName)

            try {
                details=readEncryptedFile(file)
            }catch (e: IOException){
                try {
                    val extFile = File(this.getExternalFilesDir(null),fileItem?.FileName)
                    val inputStream = FileInputStream(extFile)
                    inputStream.use{ stream->
                        details = stream.readBytes().decodeToString()
                    }
                }catch (e: IOException){
                    throw Exception(e)
                }
            }

        }
        fileDescription.text=details
        val voltarBtn = findViewById<Button>(R.id.buttonVoltar)




        voltarBtn.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(MainActivity.MAIN_ACTIVITY_EXTRA_DATA_ID, position)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
    @ExperimentalStdlibApi
    private fun readEncryptedFile(file: File): String {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        var result = ""

        val encryptedFile = EncryptedFile.Builder(
            file,
            this,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()


        encryptedFile.openFileInput().use { inputStream ->
            result = inputStream.readBytes().decodeToString()
        }

        return result
    }

}