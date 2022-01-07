package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import kotlinx.android.synthetic.main.activity_mine.*
import kotlinx.android.synthetic.main.activity_mine.account
import kotlinx.android.synthetic.main.activity_mine.name
import kotlinx.android.synthetic.main.activity_mine.times
import kotlinx.android.synthetic.main.bar.*
import android.graphics.BitmapFactory

import cn.bmob.v3.listener.DownloadFileListener

import cn.bmob.v3.datatype.BmobFile

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.Toast
import cn.bmob.v3.datatype.BmobQueryResult
import cn.bmob.v3.listener.SQLQueryListener
import kotlinx.android.synthetic.main.activity_my_team.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection


class mine : AppCompatActivity() {
    private lateinit var useraccount:String
    private lateinit var networkUtilsCoroutines: NetworkUtilsCoroutines

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine)
        supportActionBar?.hide()

        useraccount = intent.getStringExtra("account").toString()

        account.setText(useraccount)
        mine.setImageResource(R.drawable.mine_home2)
        minetext.setTextColor(Color.parseColor("#68B2A0"))

        networkUtilsCoroutines = NetworkUtilsCoroutines(this)


        team.setOnClickListener {
            val intent = Intent(this,my_team::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }

        add.setOnClickListener {
            val intent = Intent(this, new_team::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }

        home.setOnClickListener {
            val intent = Intent(this,com.example.myapplication.home::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }

        shop_.setOnClickListener {
            val intent = Intent(this, shop::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }

        talk.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }
        mycart.setOnClickListener {
            val intent = Intent(this,com.example.myapplication.mycart::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }

        my_data.setOnClickListener{
            val intent = Intent(this,com.example.myapplication.modify_Mydata::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }

        mycomment.setOnClickListener{
            val intent = Intent(this,com.example.myapplication.mycomments::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }

        order_.setOnClickListener{
            val intent = Intent(this,myorders::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }

        aboutOurs.setOnClickListener{
            val intent = Intent(this,about::class.java)
            startActivity(intent)
        }
    }


    fun setResult(bitmap: Bitmap?) {
        if (bitmap != null) {
            //Toast.makeText(this, "Load picture success!!!", Toast.LENGTH_SHORT).show()
            image_mine.setImageBitmap(bitmap)
        } else {
            Toast.makeText(this, "Can not load picture !!!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun loadPictureCoroutines(url:String) {
        // 在主线程开启一个协程
        CoroutineScope(Dispatchers.Main).launch {
            // 切换到IO 线程 - withContext 能在指定IO 线程执行完成后，切换原来的线程
            var bitmap = withContext(Dispatchers.IO) {
                networkUtilsCoroutines.loadPicture(url)
            }
            // 切换了UI 线程，更新UI
            setResult(bitmap)
        }
    }

    override fun onResume() {
        super.onResume()
        var bmobQuery: BmobQuery<User> = BmobQuery()
        bmobQuery.setLimit(500)
        bmobQuery.findObjects(object : FindListener<User>() {
            override fun done(users: MutableList<User>?, ex: BmobException?) {

                if (ex == null) {
                    if (users != null) {
                        for (user: User in users) {
                            val str1 = user.getAccount().toString()
                            val flag1 = str1.equals(useraccount)
                            if(flag1){
                                times.setText(user.getTimes().toString())

                                name.setText(user.getName().toString())

                                if(user.getTimes().toString().toInt()<=3)
                                    sport_title.text = "运动小白"
                                else if(user.getTimes().toString().toInt()<=10)
                                    sport_title.text = "运动爱好者"
                                else if(user.getTimes().toString().toInt()<=25)
                                    sport_title.text = "运动能手"
                                else
                                    sport_title.text = "运动达人"

                                val icon: BmobFile? = user.getPicture()
                                if(icon!=null)
                                    loadPictureCoroutines(icon.url.toString())

                                var team_number = 0
                                val bql = "Select * from team_table"
                                var bmobQuery: BmobQuery<team_table> = BmobQuery()
                                bmobQuery.setLimit(500)
                                bmobQuery.doSQLQuery(bql, object : SQLQueryListener<team_table>() {
                                    override fun done(teams: BmobQueryResult<team_table>?, ex: BmobException?) {
                                        if (ex == null) {
                                            if (teams != null) {
                                                for (t: team_table in teams.getResults()) {
                                                    val l = t.leader.toString()
                                                    val mem = t.member.toString()
                                                    val mem_list = mem.split(";")
                                                    var len = 0
                                                    if (l == useraccount) {
                                                        if (mem != "") {
                                                            len = mem_list.size-1
                                                        }
                                                        team_number += 1
                                                        continue
                                                    }
                                                    for (k in mem_list) {
                                                        if (k == useraccount) {
                                                            if (mem != "") {
                                                                len = mem_list.size-1
                                                            }
                                                            team_number += 1
                                                            break
                                                        }
                                                    }
                                                }
                                            }
                                            team_num.setText(team_number.toString())
                                        } else {
                                            Toast.makeText(this@mine, ex.message, Toast.LENGTH_LONG).show()
                                        }
                                    }
                                })

                            }
                        }
                    }
                }
            }
        })
    }

}