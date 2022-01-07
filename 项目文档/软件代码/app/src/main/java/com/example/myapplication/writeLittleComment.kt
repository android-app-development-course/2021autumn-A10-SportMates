package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import kotlinx.android.synthetic.main.activity_write_little_comment.*

class writeLittleComment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_little_comment)
        supportActionBar?.hide()

        send.setOnClickListener{
            val content = content.text.toString()
            if(content.length>0){
                val littlecomment = little_comments()
                littlecomment.comment_id = intent.getStringExtra("ObjID").toString()
                littlecomment.content = content
                littlecomment.account = intent.getStringExtra("account").toString()

                littlecomment.save(object : SaveListener<String>() {
                    override fun done(objectId: String?, ex: BmobException?) {
                        if (ex == null) {
                            Toast.makeText(this@writeLittleComment, "发表成功", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            Log.e("CREATE", "发表失败：" + ex.message)
                        }
                    }
                })

            }else
                Toast.makeText(this, "请输入评论内容", Toast.LENGTH_LONG).show()
        }
    }
}