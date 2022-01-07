package com.example.myapplication

import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BmobFile

class User: BmobObject() {
    var account: String? = null
    var password: String? = null
    var picture: BmobFile? = null
    var times: Int? = 0
    var sex: String? = null
    var name: String? = null

    @JvmName("getAccount1")
    fun getAccount(): String? {
        return account
    }

    @JvmName("setAccount1")
    fun setAccount(account: String) {
        this.account = account
    }

    @JvmName("getPassword1")
    fun getPassword(): String? {
        return password
    }

    @JvmName("setPassword1")
    fun setPassword(password: String) {
        this.password = password
    }

    @JvmName("getPicture1")
    fun getPicture():BmobFile?{
        return picture
    }

    @JvmName("setPicture1")
    fun setPicture(icon :BmobFile ){
        picture = icon
    }

    @JvmName("getTimes1")
    fun getTimes():Int?{
        return times
    }

    @JvmName("setTimes1")
    fun setTimes(times :Int ){
        this.times = times
    }

    @JvmName("getSex1")
    fun getSex():String?{
        return sex
    }

    @JvmName("setSex1")
    fun setSex(sex :String ){
        this.sex = sex
    }

    @JvmName("getName1")
    fun getName():String?{
        return name
    }

    @JvmName("setName1")
    fun setName(name :String ){
        this.name = name
    }


}