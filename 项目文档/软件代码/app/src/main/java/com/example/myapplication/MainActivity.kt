package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import kotlinx.android.synthetic.main.activity_comment3.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.refresh
import kotlinx.android.synthetic.main.activity_mine.*
import kotlinx.android.synthetic.main.activity_modify_mydata.*
import kotlinx.android.synthetic.main.bar.*
import kotlinx.android.synthetic.main.comment_item.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread
import android.view.MotionEvent
import android.view.View

class MainActivity : AppCompatActivity() {
    private val commentsList = ArrayList<Comment>()

    private lateinit var useraccount:String


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        useraccount = intent.getStringExtra("account").toString()

        talk.setImageResource(R.drawable.talk2)
        talktext.setTextColor(Color.parseColor("#68B2A0"))

        refresh.setColorSchemeColors(R.color.green2)
        //refresh.setDistanceToTriggerSync(1000)

        var commentQ : BmobQuery<comments> = BmobQuery()
        commentQ.setLimit(500)
        commentQ.findObjects(object :FindListener<comments>(){
            override fun done(p0: MutableList<comments>?, p1: BmobException?) {
                if(p1 == null){
                    if(p0!=null){
                        for(comment1: comments in p0){

                            var bmobQuery: BmobQuery<User> = BmobQuery()
                            bmobQuery.setLimit(500)
                            bmobQuery.findObjects(object : FindListener<User>() {
                                override fun done(users: MutableList<User>?, ex: BmobException?) {

                                    if (ex == null) {
                                        if (users != null) {
                                            for (user: User in users) {

                                                val str1 = user.getAccount().toString()
                                                val flag1 = str1.equals(comment1.account)
                                                if(flag1){

                                                    var bmobfile :BmobFile? = user.getPicture()
                                                    if(bmobfile == null)
                                                        bmobfile = BmobFile("start.jpg","","https://bmob-cdn-30303.bmobpay.com/2021/12/24/7c3daa3740c62684805d3047439ed37c.jpg")
                                                    //Comment(bmobfile!!,user.name!!,comment1.content!!,comment1.createdAt,comment1.objectId)
                                                    commentsList.add(Comment(bmobfile!!,user.name!!,comment1.content!!,comment1.createdAt,comment1.objectId,comment1.account!!))

                                                    val adapter = CommentAdapter(this@MainActivity,R.layout.comment_item,commentsList)
                                                    val listView:ListView = findViewById(R.id.listViewC)
                                                    listView.adapter = adapter

                                                    break

                                                }

                                            }
                                        }
                                    }
                                }
                            })

                        }
                    }
                }

            }
        })


        val btn_write:ImageButton = findViewById(R.id.img_write)
        btn_write.setOnClickListener {
            val intent = Intent(this,Comment2::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }

        val listView:ListView = findViewById(R.id.listViewC)
        listView.setOnItemClickListener{ parent,view,position,id->

            val commentnow = commentsList[position]
            val intent = Intent(this,comment3::class.java)
            val bundle = Bundle()
            bundle.putSerializable("commentnow", commentnow)

            intent.putExtras(bundle)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }



        mine.setOnClickListener {
            val intent = Intent(this,com.example.myapplication.mine::class.java)
            intent.putExtra("account",useraccount)
            //intent.putExtra("account",intent.getStringExtra("account").toString())
            startActivity(intent)
        }

        home.setOnClickListener {
            val intent = Intent(this,com.example.myapplication.home::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }

        add.setOnClickListener {
            val intent = Intent(this, new_team::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }
        shop_.setOnClickListener {
            val intent = Intent(this, shop::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }

        val isRefreshing = false //listview是否可用

        listView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                //判断listview第一个可见的条目是否是第一个条目
                if (listView.firstVisiblePosition == 0) {
                    val firstVisibleItemView: View = listViewC.getChildAt(0)
                    //判断第一个条目距离listview是否是0,如果是，则srLayout可用，否则不可用
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() === 0) {
                        refresh.setEnabled(true)
                    } else {
                        refresh.setEnabled(false)
                    }
                } else {
                    refresh.setEnabled(false)
                }
                //根据当前是否是在刷新数据，来决定是否拦截listview的触摸事件
                return isRefreshing
            }
        })

        refresh.setOnRefreshListener {

            thread {
                Thread.sleep(2000)
                runOnUiThread{
                    val listView:ListView = findViewById(R.id.listViewC)
                    commentsList.clear()
                    listView.adapter = CommentAdapter(this,R.layout.comment_item,commentsList)

                    var commentQ : BmobQuery<comments> = BmobQuery()
                    commentQ.setLimit(500)
                    commentQ.findObjects(object :FindListener<comments>(){
                        override fun done(p0: MutableList<comments>?, p1: BmobException?) {
                            if(p1 == null){
                                if(p0!=null){
                                    for(comment1: comments in p0){

                                        var bmobQuery: BmobQuery<User> = BmobQuery()
                                        bmobQuery.setLimit(500)
                                        bmobQuery.findObjects(object : FindListener<User>() {
                                            override fun done(users: MutableList<User>?, ex: BmobException?) {

                                                if (ex == null) {
                                                    if (users != null) {
                                                        for (user: User in users) {

                                                            val str1 = user.getAccount().toString()
                                                            val flag1 = str1.equals(comment1.account)
                                                            if(flag1){

                                                                var bmobfile :BmobFile? = user.getPicture()
                                                                if(bmobfile == null)
                                                                    bmobfile = BmobFile("start.jpg","","https://bmob-cdn-30303.bmobpay.com/2021/12/24/7c3daa3740c62684805d3047439ed37c.jpg")
                                                                //Comment(bmobfile!!,user.name!!,comment1.content!!,comment1.createdAt,comment1.objectId)
                                                                commentsList.add(Comment(bmobfile!!,user.name!!,comment1.content!!,comment1.createdAt,comment1.objectId,comment1.account!!))

                                                                val adapter = CommentAdapter(this@MainActivity,R.layout.comment_item,commentsList)
                                                                val listView:ListView = findViewById(R.id.listViewC)
                                                                listView.adapter = adapter

                                                                break

                                                            }

                                                        }
                                                    }
                                                }
                                            }
                                        })

                                    }
                                }
                            }

                        }
                    })

                    refresh.isRefreshing = false
                }
            }


        }

    }

    override fun onResume() {
        super.onResume()
        useraccount = intent.getStringExtra("account").toString()
    }



}