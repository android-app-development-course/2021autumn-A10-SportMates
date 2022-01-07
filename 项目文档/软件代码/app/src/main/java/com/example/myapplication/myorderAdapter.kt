package com.example.myapplication

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_mycart.*
import java.math.RoundingMode
import java.text.DecimalFormat

class myorderAdapter(activity: Activity, val resourceId:Int, data:List<order>):
    ArrayAdapter<order>(activity, resourceId , data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(resourceId,parent,false)


//        Toast.makeText(context,"进入",Toast.LENGTH_SHORT).show()
        var img: ImageView = view.findViewById(R.id.order_img)

        val name: TextView = view.findViewById(R.id.order_name)
        val price: TextView = view.findViewById(R.id.order_price)
        val num: TextView = view.findViewById(R.id.order_num)
        val total: TextView = view.findViewById(R.id.order_total)
        val time:TextView = view.findViewById(R.id.order_time)
        val orderNum:TextView = view.findViewById(R.id.order_id)

        val now = getItem(position)

        if(now!=null){
            name.setText(now.goods_name)
            now.image_id?.let { img.setImageResource(it) }
            price.setText(now.price.toString())
            num.setText(now.goods_num.toString())
            val format = DecimalFormat("0.#")
            //未保留小数的舍弃规则，RoundingMode.FLOOR表示直接舍弃。
            format.roundingMode = RoundingMode.FLOOR

            total.setText(format.format((now.goods_num!!* now.price!!)))
            time.setText(now.createdAt)
            orderNum.setText(now.objectId)
        }
        return view
    }
}

