package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.activity_create_order.*

class create_order : AppCompatActivity() {

    private lateinit var useraccount:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_order)
        supportActionBar?.hide()

        useraccount = intent.getStringExtra("account").toString()
        val buy_now :ArrayList<commodity> = intent.getSerializableExtra("buy_now") as ArrayList<commodity>




        val adapter = orderAdapter(this,R.layout.order_item,buy_now)
        val listView: ListView = findViewById(R.id.listViewC4)
        listView.adapter = adapter

        submit.setOnClickListener {
            val a = phone.text.toString()
            val b = name.text.toString()
            val c = address.text.toString()
            if(a.length>0&&b.length>0&&c.length>0){
                for(i in buy_now){
                    val order1 = order()
                    order1.account = useraccount
                    order1.address = c
                    order1.goods_name = i.commo_name
                    order1.goods_num = i.commo_num
                    order1.phone_num = a
                    order1.image_id = i.commo_image
                    order1.price = i.commo_price
                    order1.receive_name = b

            order1.save(object : SaveListener<String>() {
                override fun done(objectId: String?, ex: BmobException?) {
                    if (ex == null) {
                        Toast.makeText(this@create_order, "下单成功", Toast.LENGTH_LONG).show()
                        //此处跳转到购物车
                        val intent = Intent()
                        intent.putExtra("flag",true)
                        setResult(RESULT_OK,intent)
                        finish()
                    }
                }
            })
        }

            }
            else
                Toast.makeText(this,"请输入姓名、地址和电话",Toast.LENGTH_SHORT).show()
        }

    }
}
            //Toast.makeText(this,i.commo_name+i.commo_price.toString()+i.commo_image.toString(),Toast.LENGTH_SHORT).show()



//var goods_name:String?=null
//var goods_num: Int? = 0
//var phone_num: String? = null
//var address: String? = null
//var image_id: Int? = 0
//var price: Double? = 0.0
//var receive_name: String? = null