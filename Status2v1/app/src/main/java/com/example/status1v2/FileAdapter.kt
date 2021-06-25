package com.example.status1v2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.OrientationEventListener


class FileAdapter(private val fileList: List<FileItem>, private val listener: MainActivity) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.file,
        parent, false)

        return FileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val currentItem = fileList[position]
        holder.textView1.text = currentItem.FileName
 //       holder.textView2.text = currentItem.Detalhes

    }

    override fun getItemCount()= fileList.size

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val textView1: TextView = itemView.findViewById(R.id.file_name)
        val textView2: TextView = itemView.findViewById(R.id.file_data)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            var del = v?.id  == R.id.button_delete

            if(position != RecyclerView.NO_POSITION){
                if (del) {
                    listener.onDeleteButtonClick(position)
                }else{
                    listener.onItemClick(position)
                }
            }
        }
    }



    interface OnItemClickListener {
        fun onDeleteButtonClick(position: Int)
        fun onItemClick(position: Int)
    }
}