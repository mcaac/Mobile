package com.example.photoalbum

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager

class MainActivity : AppCompatActivity()  {
    companion object{
        const val REQUEST_CODE = 1
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter
    private var datalist= ArrayList<DataModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions()

        recyclerView=findViewById(R.id.recyclerView)
        recyclerView.layoutManager=GridLayoutManager(applicationContext,2)
        photoAdapter= PhotoAdapter(applicationContext)
        recyclerView.adapter= photoAdapter
        //datalist.add(DataModel("Here On Earth",R.drawable.img1))
        //datalist.add(DataModel("Fireflies",R.drawable.img2))
        datalist=getdata()
        photoAdapter.setDataList(datalist)
    }
    private fun requestPermissions(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode== REQUEST_CODE && grantResults[0]==PackageManager.PERMISSION_DENIED){
            Toast.makeText(
                this,
                "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getdata(): ArrayList<DataModel> {
        var list = ArrayList<DataModel>()
        val projection= arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )
        val selection = null
        val selectionArgs= null
        val sortOrder = null
        
        applicationContext.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, selection, selectionArgs, sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn=cursor.getColumnIndexOrThrow((MediaStore.Images.Media.DISPLAY_NAME))
            while(cursor.moveToNext()){
                val id =cursor.getLong(idColumn)
                val name=cursor.getString(nameColumn)

                Log.d("TAG","Media ID: $id")
                Log.d("TAG","Media NAME: $name")
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                )
                list.add(DataModel(name,contentUri))

            }
        }
        return list
    }
}