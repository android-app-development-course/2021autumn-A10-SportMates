package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_comment3.*
import kotlinx.android.synthetic.main.bar.*

import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import kotlinx.android.synthetic.main.activity_comment3.refresh
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.comment_item2.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread


class comment3 : AppCompatActivity() {

    private val commentsList_ = ArrayList<littlecomment>()

    private lateinit var networkUtilsCoroutines: NetworkUtilsCoroutines

    private lateinit var useraccount:String

    private lateinit var OBJID:String

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment3)
        supportActionBar?.hide()


        refresh.setColorSchemeColors(R.color.green2)


        val commentnow :Comment= intent.getSerializableExtra("commentnow") as Comment
        useraccount = intent.getStringExtra("account").toString()

        networkUtilsCoroutines = NetworkUtilsCoroutines(this)


        val name: TextView = findViewById(R.id.name)
        val comment: TextView = findViewById(R.id.comment)

        name.setText(commentnow.name)
        comment.setText(commentnow.comment)
        time.setText(commentnow.createAt)

        var bmobQuery: BmobQuery<User> = BmobQuery()
        bmobQuery.setLimit(500)
        bmobQuery.findObjects(object : FindListener<User>() {
            override fun done(users: MutableList<User>?, ex: BmobException?) {

                if (ex == null) {
                    if (users != null) {
                        for (user: User in users) {
                            val str1 = user.getAccount().toString()
                            val flag1 = str1.equals(commentnow.account)
                            if(flag1){

                                val icon: BmobFile? = user.getPicture()
                                if(icon!=null)
                                    loadPictureCoroutines(icon.url.toString())
                                else image.setImageResource(R.drawable.start)

                            }
                        }
                    }
                }
            }
        })

        val objid = commentnow.objID
        //initComment3(objid)
        OBJID = objid


        talkabout.setOnClickListener{
            val intent = Intent(this,writeLittleComment::class.java)
            intent.putExtra("account",useraccount)
            intent.putExtra("ObjID",commentnow.objID)
            startActivity(intent)
        }
        clickText.setOnClickListener{
            val intent = Intent(this,writeLittleComment::class.java)
            intent.putExtra("account",useraccount)
            intent.putExtra("ObjID",commentnow.objID)
            startActivity(intent)
        }

        val isRefreshing = false //listview是否可用

        listViewC2.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                //判断listview第一个可见的条目是否是第一个条目
                if (listViewC2.firstVisiblePosition == 0) {
                    val firstVisibleItemView: View = listViewC2.getChildAt(0)
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
                    val listView:ListView = findViewById(R.id.listViewC2)
                    commentsList_.clear()
                    listView.adapter = littlecommentAdapter(this@comment3,R.layout.littlecomment,commentsList_)

                    var bmobQ: BmobQuery<little_comments> = BmobQuery()
                    bmobQ.setLimit(500)
                    bmobQ.findObjects(object : FindListener<little_comments>() {
                        override fun done(little_comments1: MutableList<little_comments>?, ex: BmobException?) {

                            if (ex == null) {
                                if (little_comments1 != null) {
                                    for (item: little_comments in little_comments1) {

                                        val str1 = item.comment_id
                                        val flag1 = str1.equals(objid)
                                        if(flag1){

                                            var bmobQuery: BmobQuery<User> = BmobQuery()
                                            bmobQuery.setLimit(500)
                                            bmobQuery.findObjects(object : FindListener<User>() {
                                                override fun done(users: MutableList<User>?, ex: BmobException?) {

                                                    if (ex == null) {
                                                        if (users != null) {
                                                            for (user: User in users) {

                                                                val str2 = user.getAccount().toString()
                                                                val flag2 = str2.equals(item.account)
                                                                if(flag2){

                                                                    var bmobfile :BmobFile? = user.getPicture()
                                                                    if(bmobfile == null)
                                                                        bmobfile = BmobFile("start.jpg","","https://bmob-cdn-30303.bmobpay.com/2021/12/24/7c3daa3740c62684805d3047439ed37c.jpg")
                                                                    //Comment(bmobfile!!,user.name!!,comment1.content!!,comment1.createdAt,comment1.objectId)
                                                                    commentsList_.add(littlecomment(bmobfile!!,user.name!!,item.content!!,item.createdAt))

                                                                    val adapter = littlecommentAdapter(this@comment3,R.layout.littlecomment,commentsList_)

                                                                    //listView.removeAllViews()
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
                        }
                    })
                    refresh.isRefreshing = false
                }
            }

        }
    }



    fun setResult(bitmap: Bitmap?) {
        if (bitmap != null) {
            //Toast.makeText(this, "Load picture success!!!", Toast.LENGTH_SHORT).show()
            image.setImageBitmap(bitmap)
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

    fun initComment3(objid :String){
        commentsList_.clear()

        var bmobQ: BmobQuery<little_comments> = BmobQuery()
        bmobQ.setLimit(500)
        bmobQ.findObjects(object : FindListener<little_comments>() {
            override fun done(little_comments1: MutableList<little_comments>?, ex: BmobException?) {

                if (ex == null) {
                    if (little_comments1 != null) {
                        for (item: little_comments in little_comments1) {

                            val str1 = item.comment_id
                            val flag1 = str1.equals(objid)
                            if(flag1){

                                var bmobQuery: BmobQuery<User> = BmobQuery()
                                bmobQuery.setLimit(500)
                                bmobQuery.findObjects(object : FindListener<User>() {
                                    override fun done(users: MutableList<User>?, ex: BmobException?) {

                                        if (ex == null) {
                                            if (users != null) {
                                                for (user: User in users) {

                                                    val str2 = user.getAccount().toString()
                                                    val flag2 = str2.equals(item.account)
                                                    if(flag2){

                                                        var bmobfile :BmobFile? = user.getPicture()
                                                        if(bmobfile == null)
                                                            bmobfile = BmobFile("start.jpg","","https://bmob-cdn-30303.bmobpay.com/2021/12/24/7c3daa3740c62684805d3047439ed37c.jpg")
                                                        //Comment(bmobfile!!,user.name!!,comment1.content!!,comment1.createdAt,comment1.objectId)
                                                        commentsList_.add(littlecomment(bmobfile!!,user.name!!,item.content!!,item.createdAt))

                                                        val adapter = littlecommentAdapter(this@comment3,R.layout.littlecomment,commentsList_)
                                                        val listView:ListView = findViewById(R.id.listViewC2)
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
            }
        })
    }

    override fun onResume() {
        super.onResume()
        initComment3(OBJID)
    }
}