package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.UpdateListener
import kotlinx.android.synthetic.main.activity_leader_view.*
import kotlinx.android.synthetic.main.activity_leader_view.output_description
import kotlinx.android.synthetic.main.activity_leader_view.output_mem
import kotlinx.android.synthetic.main.activity_leader_view.output_name
import kotlinx.android.synthetic.main.activity_leader_view.output_place
import kotlinx.android.synthetic.main.activity_leader_view.output_time
import kotlinx.android.synthetic.main.activity_leader_view.output_type
import kotlinx.android.synthetic.main.activity_leader_view.rc
import kotlinx.android.synthetic.main.activity_team_details.*

class leader_view : AppCompatActivity() {
    private lateinit var team_id: String
    private var headList = ArrayList<head>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_view)
        supportActionBar?.hide()

        team_id = intent.getStringExtra("team_object_id").toString()

        initDetail()

        remove.setOnClickListener {
            val team = team_table()
            team.objectId = team_id
            team.delete(object : UpdateListener() {
                override fun done(ex: BmobException?) {
                    if (ex == null) {
                        Toast.makeText(this@leader_view, "解散成功", Toast.LENGTH_LONG).show()
                        finish()
                        val intent = Intent()
                        setResult(RESULT_OK, intent)
                    } else {
                        Toast.makeText(this@leader_view, ex.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
        }

        reset.setOnClickListener {
            val intent = Intent(this, update_team::class.java)
            intent.putExtra("team_object_id", team_id)
            startActivity(intent)
        }

        update_member.setOnClickListener {
            val intent = Intent(this@leader_view, del_member::class.java)
            intent.putExtra("team_object_id", team_id)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            initDetail()
        }
    }

    fun initDetail() {
        var bmobQuery: BmobQuery<team_table> = BmobQuery()
        bmobQuery.setLimit(500)
        bmobQuery.getObject(team_id, object : QueryListener<team_table>() {
            override fun done(team: team_table?, ex: BmobException?) {
                if (ex == null) {
                    output_name.text = "队 名：" + team?.name.toString()
                    output_type.text = "类 型：" + team?.type.toString()
                    var len = 0
                    var mem_list = team?.member?.split(";")
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

                    val LayoutManager = LinearLayoutManager(this@leader_view)
                    LayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                    //val LayoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                    rc.layoutManager = LayoutManager
                    val adapter = headAdapter(mem_list2!!)
                    rc.adapter = adapter //设置
                } else {
                    Toast.makeText(this@leader_view, ex.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}