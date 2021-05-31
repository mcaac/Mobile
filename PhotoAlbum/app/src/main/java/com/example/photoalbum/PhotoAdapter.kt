package com.example.photoalbum

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class PhotoAdapter(var context: Context):RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    var dataList= emptyList<DataModel>()
    internal fun setDataList(dataList : List<DataModel>){
        this.dataList=dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        var image : ImageView
        var title : TextView

        init {
            image=itemView.findViewById(R.id.imagelayout)
            title= itemView.findViewById(R.id.titleLayout)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.photo_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount()=dataList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = dataList[position]
        val images = BitmapFactory.decodeFile(data.image.getPath())
        holder.title.text=data.title
        holder.image.setImageBitmap(images)
    }
}