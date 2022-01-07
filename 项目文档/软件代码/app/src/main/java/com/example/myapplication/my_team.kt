package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobQueryResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SQLQueryListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_my_team.*
import kotlinx.android.synthetic.main.activity_my_team.listView_team

/*
我的队伍my_team.kt

根据login的账号
查user表，找到该账号的team_id
根据team_id从team表获取team信息
*/

class my_team : AppCompatActivity() {
    private lateinit var useraccount:String
    val teamList = ArrayList<Team>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_team)
        supportActionBar?.hide()

        useraccount = intent.getStringExtra("account").toString()
        initTeam();

        listView_team.setOnItemClickListener { parent, view, position, id ->
            val now = teamList[position]
            if (now.leader == useraccount) {
                val intent = Intent(this, leader_view::class.java)
                intent.putExtra("team_object_id", now.id)
                startActivityForResult(intent, 1)
            } else {
                val intent = Intent(this, team_details::class.java)
                intent.putExtra("team_object_id", now.id)
                intent.putExtra("account",useraccount)
                startActivityForResult(intent, 1)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            initTeam()
        }
    }

    private fun initTeam() {
        val bql = "Select * from team_table"
        var bmobQuery: BmobQuery<team_table> = BmobQuery()
        bmobQuery.setLimit(500)
        teamList.clear()
        bmobQuery.doSQLQuery(bql, object : SQLQueryListener<team_table>() {
            override fun done(teams: BmobQueryResult<team_table>?, ex: BmobException?) {
//                val teamList = ArrayList<Team>()
                if (ex == null) {
                    if (teams != null) {
                        for (t: team_table in teams.getResults()) {
                            val l = t.leader.toString()
                            val mem = t.member.toString()
                            val mem_list = mem.split(";")
                            val n1 = t.name.toString()
                            val p1 = t.place.toString()
                            val t1 = t.time.toString()
                            val t2 = t.type.toString()
                            val n2 = t.number.toString()
                            val i = t.objectId.toString()
                            var len = 0
                            if (l == useraccount) {
                                if (mem != "") {
                                    len = mem_list.size-1
                                }
                                teamList.add(Team(n1, p1, t1, t2, (len+1).toString() + "/" + n2, l, i))
                                val adapter = TeamAdapter(this@my_team, R.layout.team, teamList)
                                listView_team.adapter = adapter
                                continue
                            }
                            for (k in mem_list) {
                                if (k == useraccount) {
                                    if (mem != "") {
                                        len = mem_list.size-1
                                    }
                                    teamList.add(Team(n1, p1, t1, t2, (len+1).toString()+"/"+n2, l, i))
                                    val adapter = TeamAdapter(this@my_team, R.layout.team, teamList)
                                    listView_team.adapter = adapter
                                    break
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@my_team, ex.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}