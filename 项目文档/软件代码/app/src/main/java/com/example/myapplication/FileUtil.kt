package com.example.myapplication

import android.content.Context
import android.content.res.AssetManager
import java.io.*

object FileUtil{
    // 获取json文件内容
    fun getAssetsFileText(context: Context,fileName:String):String{
        val strBuilder=StringBuilder()
        val assetManager=context.assets
        val bf =BufferedReader(InputStreamReader(assetManager.open(fileName)))
        bf.use { strBuilder.append(it.readLine())}
        bf.close()
        return strBuilder.toString()
    }
}

