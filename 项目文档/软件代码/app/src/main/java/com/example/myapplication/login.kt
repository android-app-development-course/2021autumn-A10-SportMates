package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import kotlinx.android.synthetic.main.activity_login.*

class login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Bmob.initialize(this, "9221db06757499ed58a8bc4bf5dea96c");
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        //获取定位权限
        if (ContextCompat.checkSelfPermission(this,  Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
//            showmap()
//            ddd()

        }

        //选择过记住密码
        val rem : CheckBox = findViewById(R.id.select)
        val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
        if(prefs.getBoolean("remember",false)==true){
            //用户先前选择过记住密码
            rem.setChecked(true)
            account.setText(prefs.getString("account",""))
            psw.setText(prefs.getString("psw",""))
        }

        login_.setOnClickListener{
            val account = account.text
            val psw = psw.text
            var isLogin = false

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
                                    val str2 = user.getPassword().toString()
                                    val flag2 = str2.equals(psw.toString())
                                    if(flag2){
                                        Toast.makeText(this@login, "登陆成功", Toast.LENGTH_LONG).show()

                                        var editor = getSharedPreferences(
                                            "data",
                                            Context.MODE_PRIVATE
                                        ).edit()
                                        if(rem.isChecked) {
                                            editor.putString("account", account.toString())
                                            editor.putString("psw", psw.toString())
                                            editor.putBoolean("remember",true)
                                            editor.apply()
                                        }
                                        else {
                                            editor.putBoolean("remember", false)
                                            editor.apply()
                                        }

                                        isLogin = true
                                        val intent = Intent(this@login,home::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        intent.putExtra("account",account.toString())
                                        startActivity(intent)
                                        finish()

                                    }
                                    else
                                        Toast.makeText(this@login, "密码错误！", Toast.LENGTH_LONG).show()
                                }

                            }
                            if(!isLogin)
                                Toast.makeText(this@login, "账号不存在！", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@login, ex.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
        }

        register_.setOnClickListener{
            val intent = Intent(this,register::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    showmap()
//                    ddd()

                } else {

                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}