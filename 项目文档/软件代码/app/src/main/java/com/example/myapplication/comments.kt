package com.example.myapplication

import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BmobFile

class comments: BmobObject()  {
    var account: String? = null
    var content: String? = null


    @JvmName("getAccount1")
    fun getAccount(): String? {
        return account
    }

    @JvmName("setAccount1")
    fun setAccount(account: String) {
        this.account = account
    }

    @JvmName("getContent1")
    fun getContent(): String? {
        return content
    }

    @JvmName("setContent1")
    fun setContent(content: String) {
        this.content = content
    }


}