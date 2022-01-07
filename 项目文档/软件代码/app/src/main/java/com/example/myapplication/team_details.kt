package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.UpdateListener
import kotlinx.android.synthetic.main.activity_leader_view.*
import kotlinx.android.synthetic.main.activity_team_details.*
import kotlinx.android.synthetic.main.activity_team_details.output_description
import kotlinx.android.synthetic.main.activity_team_details.output_mem
import kotlinx.android.synthetic.main.activity_team_details.output_name
import kotlinx.android.synthetic.main.activity_team_details.output_place
import kotlinx.android.synthetic.main.activity_team_details.output_time
import kotlinx.android.synthetic.main.activity_team_details.output_type
import kotlinx.android.synthetic.main.activity_team_details.rc

/*
队伍详情team_detail.kt

点击队伍(指队伍列表的队伍)[首页/我的队伍]
判断该login的账号是否在队伍中，否则bottom显示“加入队伍”
判断该login的账号是否为队长，是则bottom显示“修改队伍”
判断该login的账号是否为队员，是则bottom显示“退出队伍”
*/

class team_details : AppCompatActivity() {
    private lateinit var team_id: String
    private lateinit var useraccount:String
    private var headList = ArrayList<head>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_details)
        supportActionBar?.hide()

        team_id = intent.getStringExtra("team_object_id").toString()
        useraccount = intent.getStringExtra("account").toString()

        var flag = true
        var member_now: String = ""

        var bmobQuery: BmobQuery<team_table> = BmobQuery()
        bmobQuery.setLimit(500)
        bmobQuery.getObject(team_id, object : QueryListener<team_table>() {
            override fun done(team: team_table?, ex: BmobException?) {
                if (ex == null) {
                    output_name.text = "队 名：" + team?.name.toString()
                    output_type.text = "类 型：" + team?.type.toString()
                    var len = 0
                    member_now = team?.member.toString()
                    var mem_list = team?.member?.split(";")
                    if (mem_list != null) {
                        for (j in mem_list) {
                            if (j == useraccount) {
                                bt.text = "退出队伍"
                                flag = false
                            }
                        }
                    }
                    if (team?.member != "") {
                        if (mem_list != null) {
                            len = mem_list.size-1
                        }
                    }
                    output_mem.text = "人 数：" + (len+1).toString() + "/" + team?.number
                    output_time.text = "时 间：" + team?.time.toString()
                    output_place.text = "地 点：" + team?.place.toString()
                    output_description.text = team?.description.toString()

                    var ld = team?.leader.toString()

                    var mem_list2 = ArrayList<String>()

                    val iterator = mem_list?.iterator()
                    if (iterator != null) {
                        while (iterator.hasNext()) {
                            val value = iterator.next()
                            if (value.length>0) {
                                mem_list2 = mem_list2?.plus(value) as ArrayList<String>
                            }
                        }
                    }

                    mem_list2 = mem_list2?.plus(ld) as ArrayList<String>

                    val LayoutManager = LinearLayoutManager(this@team_details)
                    LayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                    rc.layoutManager = LayoutManager
                    val adapter = headAdapter(mem_list2!!)
                    rc.adapter = adapter //设置

//                    var bmobQuery2: BmobQuery<User> = BmobQuery()
//                    bmobQuery2.findObjects(object : FindListener<User>() {
//                        override fun done(users: MutableList<User>?, ex: BmobException?) {
//                            if (ex == null) {
//                                if (users != null) {
//                                    for (user: User in users) {
//                                        val user_id = user.account.toString()
//                                        val bf = user.picture
//                                        val nm = user.name
//                                        if (mem_list != null) {
//                                            for (tm in mem_list) {
//                                                if (tm == user_id) {
//                                                    headList.add(head(bf!!))
//                                                    break
//                                                }
//                                            }
//                                        }
////                                        if (user_id == ld) {
////                                            headList.add(head(bf!!))
////                                        }
//                                        val LayoutManager = LinearLayoutManager(this@team_details)
//                                        LayoutManager.orientation = LinearLayoutManager.HORIZONTAL
//                                        rc.layoutManager = LayoutManager
//                                        val adapter = headAdapter(headList)
//                                        rc.adapter = adapter //设置
//                                    }
//                                }
//                            } else {
//                                Toast.makeText(this@team_details, ex.message, Toast.LENGTH_LONG).show()
//                            }
//                        }
//                    })

                } else {
                    Toast.makeText(this@team_details, ex.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        bt.setOnClickListener {
            if (flag) { //加入队伍
                member_now = "$member_now$useraccount;"
                val team = team_table()
                team.objectId = team_id
                team.member = member_now
                team.update(object : UpdateListener() {
                    override fun done(ex: BmobException?) {
                        if (ex == null) {
                            Toast.makeText(this@team_details, "成功加入", Toast.LENGTH_LONG).show()
                            finish()
                            val intent = Intent(this@team_details, team_details::class.java)
                            intent.putExtra("team_object_id", team_id)
                            intent.putExtra("account",useraccount)
                            setResult(RESULT_OK, intent)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@team_details, ex.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
            } else { //退出队伍
                member_now = member_now.replace("$useraccount;", "")
                val team = team_table()
                team.objectId = team_id
                team.member = member_now
                team.update(object : UpdateListener() {
                    override fun done(ex: BmobException?) {
                        if (ex == null) {
                            Toast.makeText(this@team_details, "成功退出", Toast.LENGTH_LONG).show()
                            val intent = Intent()
                            setResult(RESULT_OK, intent)
                            finish()
                        } else {
                            Toast.makeText(this@team_details, ex.message, Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}