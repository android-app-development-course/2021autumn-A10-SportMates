package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.UpdateListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_mycomments.*
import kotlinx.android.synthetic.main.activity_mycomments.refresh
import kotlin.concurrent.thread

class mycomments : AppCompatActivity() {
    private val commentsList_ = ArrayList<comments>()
    private val commentsList = ArrayList<little_comments>()
    private var flag:Int = 1

    private lateinit var useraccount:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mycomments)
        supportActionBar?.hide()

        useraccount = intent.getStringExtra("account").toString()

        big.setBackgroundResource(R.drawable.button_styles)

        var bmobQuery: BmobQuery<comments> = BmobQuery()
        bmobQuery.setLimit(500)
        bmobQuery.findObjects(object : FindListener<comments>() {
            override fun done(items: MutableList<comments>?, ex: BmobException?) {

                if (ex == null) {
                    if (items != null) {
                        for (item: comments in items) {

                            val str1 = item.getAccount().toString()
                            val flag1 = str1.equals(useraccount)
                            if(flag1){
                                commentsList_.add(item)

                                val adapter = bigcommentAdapter(this@mycomments,R.layout.mybigcomment,commentsList_!!)
                                listViewC3.adapter = adapter
                            }

                        }
                    }
                }
            }
        })

        big.setOnClickListener{
            big.setBackgroundResource(R.drawable.button_styles)
            small.setBackgroundResource(R.drawable.btn_selector)

            flag = 1
            commentsList_.clear()
            listViewC3.adapter = bigcommentAdapter(this@mycomments,R.layout.mybigcomment,commentsList_)

            var bmobQuery: BmobQuery<comments> = BmobQuery()
            bmobQuery.setLimit(500)
            bmobQuery.findObjects(object : FindListener<comments>() {
                override fun done(items: MutableList<comments>?, ex: BmobException?) {

                    if (ex == null) {
                        if (items != null) {
                            for (item: comments in items) {

                                val str1 = item.getAccount().toString()
                                val flag1 = str1.equals(useraccount)
                                if(flag1){
                                    commentsList_.add(item)

                                    val adapter = bigcommentAdapter(this@mycomments,R.layout.mybigcomment,commentsList_!!)
                                    listViewC3.adapter = adapter
                                }

                            }
                        }
                    }
                }
            })


        }

        small.setOnClickListener{
            small.setBackgroundResource(R.drawable.button_styles)
            big.setBackgroundResource(R.drawable.btn_selector)


            commentsList.clear()
            listViewC3.adapter = smallcommentAdapter(this@mycomments,R.layout.mysmallcomment,commentsList)

            flag = 0

            var bmobQuery: BmobQuery<little_comments> = BmobQuery()
            bmobQuery.setLimit(500)
            bmobQuery.findObjects(object : FindListener<little_comments>() {
                override fun done(items: MutableList<little_comments>?, ex: BmobException?) {

                    if (ex == null) {
                        if (items != null) {
                            for (item: little_comments in items) {

                                val str1 = item.getAccount().toString()
                                val flag1 = str1.equals(useraccount)
                                if(flag1){
                                    commentsList.add(item)

                                    val adapter = smallcommentAdapter(this@mycomments,R.layout.mysmallcomment,commentsList)
                                    listViewC3.adapter = adapter
                                }

                            }
                        }
                    }
                }
            })
        }


        listViewC3.setOnItemClickListener{
            parent,view,position,id->
            if(flag == 1){
                val item = commentsList_[position]
                AlertDialog.Builder(this).apply {
                    setTitle("????????????")
                    setMessage("?????????????????????????????????????????????")
                    setCancelable(false)
                    setPositiveButton("???"){dialog,which->
                        item.delete(object : UpdateListener() {
                            override fun done(ex: BmobException?) {
                                if (ex == null) {
                                    Toast.makeText(this@mycomments, "????????????", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(this@mycomments, ex.message, Toast.LENGTH_LONG).show()
                                }

                                commentsList_.clear()
                                listViewC3.adapter = bigcommentAdapter(this@mycomments,R.layout.mybigcomment,commentsList_)

                                var bmobQuery: BmobQuery<comments> = BmobQuery()
                                bmobQuery.setLimit(500)
                                bmobQuery.findObjects(object : FindListener<comments>() {
                                    override fun done(items: MutableList<comments>?, ex: BmobException?) {

                                        if (ex == null) {
                                            if (items != null) {
                                                for (item: comments in items) {

                                                    val str1 = item.getAccount().toString()
                                                    val flag1 = str1.equals(useraccount)
                                                    if(flag1){
                                                        commentsList_.add(item)

                                                        val adapter = bigcommentAdapter(this@mycomments,R.layout.mybigcomment,commentsList_!!)
                                                        listViewC3.adapter = adapter
                                                    }

                                                }
                                            }
                                        }
                                    }
                                })
                            }
                        })
                    }
                    setNegativeButton("???"){dialog,which->

                    }
                    show()
                }
            }else{
                val item = commentsList[position]
                AlertDialog.Builder(this).apply {
                    setTitle("????????????")
                    setMessage("?????????????????????????????????????????????")
                    setCancelable(false)
                    setPositiveButton("???"){dialog,which->
                        item.delete(object : UpdateListener() {
                            override fun done(ex: BmobException?) {
                                if (ex == null) {
                                    Toast.makeText(this@mycomments, "????????????", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(this@mycomments, ex.message, Toast.LENGTH_LONG).show()
                                }

                                commentsList.clear()
                                listViewC3.adapter = smallcommentAdapter(this@mycomments,R.layout.mysmallcomment,commentsList)

                                var bmobQuery: BmobQuery<little_comments> = BmobQuery()
                                bmobQuery.setLimit(500)
                                bmobQuery.findObjects(object : FindListener<little_comments>() {
                                    override fun done(items: MutableList<little_comments>?, ex: BmobException?) {

                                        if (ex == null) {
                                            if (items != null) {
                                                for (item: little_comments in items) {

                                                    val str1 = item.getAccount().toString()
                                                    val flag1 = str1.equals(useraccount)
                                                    if(flag1){
                                                        commentsList.add(item)

                                                        val adapter = smallcommentAdapter(this@mycomments,R.layout.mysmallcomment,commentsList)
                                                        listViewC3.adapter = adapter
                                                    }

                                                }
                                            }
                                        }
                                    }
                                })
                            }
                        })
                    }
                    setNegativeButton("???"){dialog,which->

                    }
                    show()
                }
            }
        }


        val isRefreshing = false //listview????????????

        listViewC3.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                //??????listview????????????????????????????????????????????????
                if (listViewC3.firstVisiblePosition == 0) {
                    val firstVisibleItemView: View = listViewC3.getChildAt(0)
                    //???????????????????????????listview?????????0,???????????????srLayout????????????????????????
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() === 0) {
                        refresh.setEnabled(true)
                    } else {
                        refresh.setEnabled(false)
                    }
                } else {
                    refresh.setEnabled(false)
                }
                //????????????????????????????????????????????????????????????listview???????????????
                return isRefreshing
            }
        })



        refresh.setOnRefreshListener {

            thread {
                Thread.sleep(2000)
                runOnUiThread{
                    if(flag==1){
                        commentsList_.clear()
                        listViewC3.adapter = bigcommentAdapter(this@mycomments,R.layout.mybigcomment,commentsList_)

                        var bmobQuery: BmobQuery<comments> = BmobQuery()
                        bmobQuery.setLimit(500)
                        bmobQuery.findObjects(object : FindListener<comments>() {
                            override fun done(items: MutableList<comments>?, ex: BmobException?) {

                                if (ex == null) {
                                    if (items != null) {
                                        for (item: comments in items) {

                                            val str1 = item.getAccount().toString()
                                            val flag1 = str1.equals(useraccount)
                                            if(flag1){
                                                commentsList_.add(item)

                                                val adapter = bigcommentAdapter(this@mycomments,R.layout.mybigcomment,commentsList_!!)
                                                listViewC3.adapter = adapter
                                            }

                                        }
                                    }
                                }
                            }
                        })
                    }
                    else{
                        commentsList.clear()
                        listViewC3.adapter = smallcommentAdapter(this@mycomments,R.layout.mysmallcomment,commentsList)

                        var bmobQuery: BmobQuery<little_comments> = BmobQuery()
                        bmobQuery.setLimit(500)
                        bmobQuery.findObjects(object : FindListener<little_comments>() {
                            override fun done(items: MutableList<little_comments>?, ex: BmobException?) {

                                if (ex == null) {
                                    if (items != null) {
                                        for (item: little_comments in items) {

                                            val str1 = item.getAccount().toString()
                                            val flag1 = str1.equals(useraccount)
                                            if(flag1){
                                                commentsList.add(item)

                                                val adapter = smallcommentAdapter(this@mycomments,R.layout.mysmallcomment,commentsList)
                                                listViewC3.adapter = adapter
                                            }

                                        }
                                    }
                                }
                            }
                        })
                    }

                    refresh.isRefreshing = false
                }
            }

        }

    }


}