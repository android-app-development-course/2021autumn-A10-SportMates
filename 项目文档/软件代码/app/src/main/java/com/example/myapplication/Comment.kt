package com.example.myapplication

import android.graphics.Bitmap
import cn.bmob.v3.datatype.BmobFile
import java.io.Serializable

class Comment(val img:BmobFile?, val name:String, val comment :String,val createAt :String,val objID:String,val account :String):Serializable{
    private val serialVersionUID = 1L
}