package com.example.myapplication

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class commoAdapter (activity: Activity, val resourceId: Int, data: List<commodity>) :
    ArrayAdapter<commodity>(activity, resourceId, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(resourceId,parent,false)
        val image:ImageView = view.findViewById(R.id.commo_image)
        val price: TextView = view.findViewById(R.id.commo_price)
        val name: TextView = view.findViewById(R.id.commo_name)
        val num: TextView = view.findViewById(R.id.commo_num)
        val type: TextView = view.findViewById(R.id.commo_type)
        val now = getItem(position)
        if(now != null) {
            image.setImageResource(now.commo_image)
            price.setText(now.commo_price.toString())
            name.setText(now.commo_name)
            num.setText(now.commo_num.toString())
            type.setText(now.commo_type)
        }
        return view
    }
}