package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobQueryResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SQLQueryListener
import cn.bmob.v3.listener.UpdateListener
import kotlinx.android.synthetic.main.activity_del_member.*

class del_member : AppCompatActivity() {
    val memberList = ArrayList<member>()

    private lateinit var team_id: String
    var m1 = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_del_member)
        supportActionBar?.hide()

        team_id = intent.getStringExtra("team_object_id").toString()

        initMember()
//        val adapter = memberAdapter(this, R.layout.member, memberList)
//        listView_member.adapter = adapter

        listView_member.setOnItemClickListener{
                parent,view,position,id->

//            Toast.makeText(this@del_member, "点击", Toast.LENGTH_LONG).show()

            val item = m1[position]
            AlertDialog.Builder(this).apply {
                setTitle("删除成员")
                setMessage("请问您是否确定要删除该成员？")
                setCancelable(false)

                var member_now: String = ""

                var bmobQuery: BmobQuery<team_table> = BmobQuery()
                bmobQuery.setLimit(500)
                bmobQuery.getObject(team_id, object : QueryListener<team_table>() {
                    override fun done(team: team_table?, ex: BmobException?) {
                        if (ex == null) {
                            member_now = team?.member.toString()
                        } else {
                            Toast.makeText(this@del_member, ex.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })

                val team = team_table()
                team.objectId = team_id
                member_now = member_now.replace("$item;", "")
                team.member = member_now
                setPositiveButton("是"){ dialog, which->
                    team.update(object : UpdateListener() {
                        override fun done(ex: BmobException?) {
                            if (ex == null) {
                                Toast.makeText(this@del_member, "删除成功", Toast.LENGTH_LONG).show()
                                initMember()
                                setResult(RESULT_OK, intent)
                            } else {
                                Toast.makeText(this@del_member, ex.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                }
                setNegativeButton("否"){ dialog, which->
                }
                show()
            }
        }

        back.setOnClickListener {
            finish()
        }
    }

    fun initMember() {
        var team_member: String = ""

        val bql = "Select * from team_table"
        m1.clear()
        var bmobQuery1: BmobQuery<team_table> = BmobQuery()
        bmobQuery1.setLimit(500)
        bmobQuery1.doSQLQuery(bql, object : SQLQueryListener<team_table>() {
            override fun done(teams: BmobQueryResult<team_table>?, ex: BmobException?) {
                if (ex == null) {
                    if (teams != null) {
                        for (t: team_table in teams.getResults()) {
                            val id = t.objectId.toString()
                            if (id == team_id) {
                                team_member = t.member.toString()

                                var m = team_member.split(";")

                                val iterator = m?.iterator()
                                if (iterator != null) {
                                    while (iterator.hasNext()) {
                                        val value = iterator.next()
                                        if (value.length>0) {
                                            m1 = m1?.plus(value) as ArrayList<String>
                                        }
                                    }
                                }

                                val adapter = memberAdapter(this@del_member, R.layout.member, m1)
                                listView_member.adapter = adapter

                                break
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@del_member, ex.message, Toast.LENGTH_LONG).show()
                }
            }
        })

    }
}