package com.example.myapplication

import cn.bmob.v3.BmobObject

class order : BmobObject() {
    var account: String? = null
    var goods_name:String?=null
    var goods_num: Int? = 0
    var phone_num: String? = null
    var address: String? = null
    var image_id: Int? = 0
    var price: Double? = 0.0
    var receive_name: String? = null
}