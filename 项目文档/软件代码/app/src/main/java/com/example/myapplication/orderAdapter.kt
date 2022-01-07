package com.example.myapplication

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import cn.bmob.v3.Bmob

class orderAdapter(activity: Activity, val resourceId:Int, data:List<commodity>):
    ArrayAdapter<commodity>(activity, resourceId , data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(resourceId,parent,false)

        var img: ImageView = view.findViewById(R.id.order_img)

        val name: TextView = view.findViewById(R.id.order_name)
        val price: TextView = view.findViewById(R.id.order_price)
        val num: TextView = view.findViewById(R.id.order_num)
        val total: TextView = view.findViewById(R.id.order_total)

        val now = getItem(position)

        if(now!=null){
            name.setText(now.commo_name)
            img.setImageResource(now.commo_image)
            price.setText(now.commo_price.toString())
            num.setText(now.commo_num.toString())
            total.setText((now.commo_price*now.commo_num).toString())
        }
        return view
    }

}