package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.UpdateListener
import kotlinx.android.synthetic.main.activity_update_team.*

class update_team : AppCompatActivity() {
    private lateinit var team_id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_team)
        supportActionBar?.hide()

        team_id = intent.getStringExtra("team_object_id").toString()

        var bmobQuery: BmobQuery<team_table> = BmobQuery()
        bmobQuery.setLimit(500)
        bmobQuery.getObject(team_id, object : QueryListener<team_table>() {
            override fun done(team: team_table?, ex: BmobException?) {
                if (ex == null) {
                    edit_name.setText(team?.name.toString())
                    edit_type.setText(team?.type.toString())
                    edit_num.setText(team?.number.toString())
                    edit_time.setText(team?.time.toString())
                    edit_place.setText(team?.place.toString())
                    edit_description.setText(team?.description.toString())
                } else {
                    Toast.makeText(this@update_team, ex.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        okk.setOnClickListener {
            val team = team_table()
            team.objectId = team_id
            team.name = edit_name.text.toString()
            team.type = edit_type.text.toString()
            team.number = edit_num.text.toString().toInt()
            team.time = edit_time.text.toString()
            team.place = edit_place.text.toString()
            team.description = edit_description.text.toString()
            team.update(object : UpdateListener() {
                override fun done(ex: BmobException?) {
                    if (ex == null) {
                        Toast.makeText(this@update_team, "成功编辑", Toast.LENGTH_LONG).show()
                        finish()
                        val intent = Intent(this@update_team, leader_view::class.java)
                        intent.putExtra("team_object_id", team_id)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@update_team, ex.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
        }

        no_okk.setOnClickListener {
            finish()
        }
    }
}