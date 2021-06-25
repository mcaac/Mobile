package com.example.status1v2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Context
import android.content.Intent
import android.widget.*
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import java.io.FileOutputStream
import java.io.File

class MainActivity : AppCompatActivity(), FileAdapter.OnItemClickListener  {

    companion object {
        const val MAIN_ACTIVITY_RESULT_CODE = 1
        const val MAIN_ACTIVITY_EXTRA_DATA_ID = "MAIN_ACTIVITY_EXTRA_DATA_ID"
        const val MAIN_ACTIVITY_EXTRA_POSITION_ID = "MAIN_ACTIVITY_EXTRA_DATA_POSITION_ID"
        const val MAIN_ACTIVITY_EXTRA_ARM_ID = "MAIN_ACTIVITY_EXTRA_ARM_ID"

    }
//    private static final String FILE_NAME="example.txt";

    private var fileList = ArrayList<FileItem>()

    var armazenamento: String = ""
    var jetpack: String = ""

    val FilenameInput = findViewById<EditText>(R.id.file_name)
    val FileName = FilenameInput.text.toString();
    val DetalhesInput = findViewById<EditText>(R.id.file_data)
    val Detalhes = DetalhesInput.text.toString();
    val fileItem = FileItem(FileName, Detalhes)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.adapter = FileAdapter(fileList, this)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        val button: View = findViewById(R.id.button)

        button.setOnClickListener {
            creatingFile(recycler_view)
        }

    }


    fun onRadioButtonClicked(view: View) {
        val btnradio = view.findViewById<RadioGroup>(R.id.radio_group_IE)

        if (btnradio.getCheckedRadioButtonId() == radioInt.id) {
            armazenamento = "I"
        } else {
            armazenamento = "E"
        }

        if (checkBox.isChecked) {
            jetpack = "T"
        } else {
            jetpack = "F"
        }

    }

    private fun deleteItem(view: View?, index: Int) {
        fileList.removeAt(index)
        recycler_view.adapter?.notifyItemRemoved(index)
    }

    override fun onDeleteButtonClick(index: Int) {
        val file = fileList[index]
        deleteItem(null, index)
        if (armazenamento == "I") {
            this.deleteFile(file.FileName)
        } else {
            val EFile = File(this.getExternalFilesDir(null), file.FileName)
            EFile.delete()
        }
    }

    fun creatingFile(view: View) {
        val FilenameInput = view.findViewById<EditText>(R.id.file_name)
        val FileName = FilenameInput.text.toString();
        val DetalhesInput = view.findViewById<EditText>(R.id.file_data)
        val Detalhes = DetalhesInput.text.toString();
        FilenameInput.text.clear()
        DetalhesInput.text.clear()

        fileList.add(
            FileItem(
                FileName,
                Detalhes
            )
        )
        if (armazenamento == "I") {
            if (jetpack == "T") {
                this.InternalSave(FileName, Detalhes)

            } else {
                val file = File(getExternalFilesDir(null), FileName)

                               EncryptedSave(file, Detalhes)

            }
        }else if(armazenamento == "E"){
            if (jetpack == "T") {
                this.ExternalSave(FileName, Detalhes)

            } else {
                val file = File(getExternalFilesDir(null), FileName)

                               EncryptedSave(file, Detalhes)

            }
        }


        val fileSize = fileList.size
        recycler_view.adapter?.notifyItemInserted(fileSize)
    }


    public fun InternalSave(fileName: String, Detalhes: String) {
        try {
            openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it.write(Detalhes.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace();
        }

    }

    public fun ExternalSave(fileName: String, Detalhes: String) {
        val extFile = File(this.getExternalFilesDir(null), fileName)
        val outputStream = FileOutputStream(extFile)
        try {
            outputStream.use { stream ->
                stream.write(Detalhes.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }
    public fun EncryptedSave(fileName: File, Detalhes: String) {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        if(fileName.exists()) {
            fileName.delete()
        }
        val encryptedFile = EncryptedFile.Builder(
            fileName,
            this,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        encryptedFile.openFileOutput().use { writer ->
            writer.write(Detalhes.toByteArray())
        }
    }
    override fun onItemClick(index: Int) {
        val file: FileItem = fileList[index]

        val intent = Intent(this, Details::class.java)
        intent.putExtra(MAIN_ACTIVITY_EXTRA_DATA_ID, file)
        intent.putExtra(MAIN_ACTIVITY_EXTRA_ARM_ID,armazenamento)
        startActivityForResult(intent, MAIN_ACTIVITY_RESULT_CODE)
    }
}



















