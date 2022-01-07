package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobObject
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.datatype.BmobQueryResult
import cn.bmob.v3.listener.QueryListener

import cn.bmob.v3.listener.SQLQueryListener
import cn.bmob.v3.listener.SaveListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.account
import kotlinx.android.synthetic.main.activity_register.psw


class register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        register.setOnClickListener{

            val account = account.text.toString()
            val psw = psw.text.toString()

            if(account.length>=8){
                if(psw.length>=6){
                    var flag = true
                    var bmobQuery: BmobQuery<User> = BmobQuery()
                    bmobQuery.setLimit(500)
                    bmobQuery.findObjects(object : FindListener<User>() {
                        override fun done(users: MutableList<User>?, ex: BmobException?) {
                            if (ex == null) {
                                if (users != null) {
                                    for (user: User in users) {
                                        val str1 = user.getAccount().toString()
                                        val flag1 = str1.equals(account.toString())
                                        if(flag1){
                                            flag = false
                                            Toast.makeText(this@register, "账号已存在！", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                            }
                            if(flag){
                                val user = User()
                                user.account = account
                                user.password = psw

                                user.save(object : SaveListener<String>() {
                                    override fun done(objectId: String?, ex: BmobException?) {
                                        if (ex == null) {
                                            Toast.makeText(this@register, "注册成功", Toast.LENGTH_LONG).show()
                                            val intent = Intent(this@register,login::class.java)
                                            startActivity(intent)
                                        } else {
                                            Log.e("CREATE", "注册失败：" + ex.message)
                                        }
                                    }
                                })
                            }
                        }
                    })


                }else{
                    Toast.makeText(this@register, "密码过短，请设置六位及以上！", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this@register, "账号过短，请设置八位及以上！", Toast.LENGTH_LONG).show()
            }
        }
    }


}


