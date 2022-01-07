package com.example.myapplication

import cn.bmob.v3.BmobObject

class little_comments: BmobObject()  {
    var account: String? = null
    var content: String? = null
    var comment_id: String? = null

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

    @JvmName("getComment_id1")
    fun getComment_id(): String? {
        return comment_id
    }

    @JvmName("setComment_id1")
    fun setComment_id(comment_id: String) {
        this.comment_id = comment_id
    }
}