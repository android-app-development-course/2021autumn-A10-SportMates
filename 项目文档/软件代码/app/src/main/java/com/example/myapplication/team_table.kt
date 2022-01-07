package com.example.myapplication

import cn.bmob.v3.BmobObject

class team_table : BmobObject() {
    var name: String? = null
    var type: String? = null
    var leader: String? = null
    var member: String? = null // 队员以;隔开
    var number: Int? = null
    var time: String? = null
    var place: String? = null
    var description: String? = null
}
