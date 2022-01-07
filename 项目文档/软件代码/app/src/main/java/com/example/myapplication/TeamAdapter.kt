package com.example.myapplication

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class TeamAdapter(activity:Activity, val resourceId: Int, data:List<Team>):
    ArrayAdapter<Team>(activity, resourceId , data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(resourceId,parent,false)
        val name:TextView = view.findViewById(R.id.team_name)
        val place:TextView = view.findViewById(R.id.team_place)
        val time:TextView = view.findViewById(R.id.team_time)
        val type:TextView = view.findViewById(R.id.team_type)
        val num:TextView = view.findViewById(R.id.team_num)
        val now = getItem(position)
        if(now != null) {
            name.setText(now.name)
            place.setText(now.place)
            time.setText(now.time)
            type.setText(now.type)
            num.setText(now.num)
        }
        return view
    }
}