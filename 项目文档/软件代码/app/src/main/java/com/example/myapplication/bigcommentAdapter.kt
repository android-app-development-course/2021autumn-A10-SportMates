package com.example.myapplication

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import android.widget.TextView
import cn.bmob.v3.Bmob

class bigcommentAdapter(activity: Activity, val resourceId:Int, data:List<comments>):
    ArrayAdapter<comments>(activity, resourceId , data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View{
        val view = LayoutInflater.from(context).inflate(resourceId,parent,false)

        val content :TextView = view.findViewById(R.id.content)
        val time:TextView = view.findViewById(R.id.time)

        val now = getItem(position)

        if(now!=null){
            content.setText(now.content)
            time.setText(now.createdAt)
        }
        return view
    }
}