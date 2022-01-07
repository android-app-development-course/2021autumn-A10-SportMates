package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import kotlinx.android.synthetic.main.activity_comment2.*

class Comment2 : AppCompatActivity() {

    private lateinit var useraccount:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment2)
        supportActionBar?.hide()

        useraccount = intent.getStringExtra("account").toString()

        val btn_send:Button = findViewById(R.id.send)
        btn_send.setOnClickListener{
            if(content.text.toString().length>0){
                val comment = comments()
                comment.account = intent.getStringExtra("account")
                comment.content = content.text.toString()

                comment.save(object : SaveListener<String>() {
                    override fun done(objectId: String?, ex: BmobException?) {
                        if (ex == null) {
                            Toast.makeText(this@Comment2, "发表成功！", Toast.LENGTH_LONG).show()
                            //val intent = Intent(this@Comment2,MainActivity::class.java)
                            //intent.putExtra("account",useraccount)
                            //startActivity(intent)
                            finish()
                        } else {
                            Log.e("CREATE", "发表失败：" + ex.message)
                        }
                    }
                })
            }
            else
                Toast.makeText(this,"请输入内容！",Toast.LENGTH_LONG).show()


            //val intent = Intent(this,MainActivity::class.java)
            //startActivity(intent)
        }
    }
}