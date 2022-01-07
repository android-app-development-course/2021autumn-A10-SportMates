package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_goods_detail.*

class goods_detail : AppCompatActivity() {
    private lateinit var useraccount:String

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_detail)
        supportActionBar?.hide()

        useraccount = intent.getStringExtra("account").toString()

        val goods_now :goods= intent.getSerializableExtra("goods_now") as goods
        goods_detail1.setImageResource(goods_now.goods_imag_id)
        goods_detail2.setImageResource(goods_now.goods_image1)
        goods_detail3.setImageResource(goods_now.goods_image2)
        goods_describe.setText(goods_now.goods_describe)
        goods_name.setText(goods_now.goods_name)
        goods_money.setText(goods_now.goods_price.toString())

        val DBhelper = MyDatabaseHelper(this,"Cart.db",1)

        val db = DBhelper.writableDatabase
//        db.execSQL("delete from Cart")

        fun share_text(context: Context, text:String){
            val intent = Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(intent, "分享"));
        }
        add_toCart.setOnClickListener {

            val db = DBhelper.writableDatabase

            // 查询Book表中所有的数据
            var flag = false
            val cursor = db.query("Cart", null, null, null, null, null, null)
            if (cursor.moveToFirst()) {
                do {
                    // 遍历Cursor对象，取出数据并打印
                    val name = cursor.getString(cursor.getColumnIndex("name"))
                    if(name.equals(goods_name.text.toString())){
                        db.execSQL("update Cart set num = ? where name = ?", arrayOf(cursor.getInt(cursor.getColumnIndex("num"))+1,name))
                        flag = true
                        break
                    }

                } while (cursor.moveToNext())
            }
            cursor.close()

            if(!flag){
                db.execSQL("insert into Cart (name,image,price,num) values(?,?,?,?)", arrayOf(goods_name.text.toString(),goods_now.goods_imag_id,goods_money.text.toString(),1))
            }

            Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show()

        }

        to_cart.setOnClickListener {
            val intent = Intent(this, mycart::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }

        share.setOnClickListener {
            share_text(this,goods_now.goods_name+"￥"+goods_now.goods_price+"@来自SportMates")
        }
    }
}