package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener

class myorders : AppCompatActivity() {
    private val orderList = ArrayList<order>()
    private lateinit var useraccount:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myorders)

        supportActionBar?.hide()

        useraccount = intent.getStringExtra("account").toString()

        var bmobQuery: BmobQuery<order> = BmobQuery()
        bmobQuery.setLimit(500)
        bmobQuery.findObjects(object : FindListener<order>() {
            override fun done(orders: MutableList<order>?, ex: BmobException?) {
                if (ex == null) {
                    if (orders != null) {
                        for (order1: order in orders) {

                            val str1 = order1.account
                            val flag1 = str1.equals(useraccount)
                            if(flag1){
                                orderList.add(order1)

                                val adapter = myorderAdapter(this@myorders,R.layout.myoders_item,orderList)
                                val listView: ListView = findViewById(R.id.myorderList)
                                listView.adapter = adapter

                            }

                        }
                    }
                }
            }
        })
    }
}