package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import cn.bmob.v3.Bmob
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.DownloadFileListener
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class NetworkUtilsCoroutines {

    private var resultPicture: Bitmap? = null
    private var context: Context

    companion object {
        const val TAG = "NetworkUtilsCoroutines"
    }

    constructor(context: Context) {
        this.context = context
    }

    // 获取网络图片
    fun loadPicture(url: String): Bitmap? {
        // 开启一个单独线程进行网络读取

        var bitmapFromNetwork: Bitmap? = null
        try {
            // 根据URL 实例， 获取HttpURLConnection 实例
                val url1 = URL(url)
            var httpURLConnection: HttpURLConnection = url1.openConnection() as HttpURLConnection
            // 设置读取 和 连接 time out 时间
            httpURLConnection.readTimeout = 2000
            httpURLConnection.connectTimeout = 2000
            // 获取图片输入流
            var inputStream = httpURLConnection.inputStream
            // 获取网络响应结果
            var responseCode = httpURLConnection.responseCode

            // 获取正常
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 解析图片
                bitmapFromNetwork = BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: IOException) { // 捕获异常 (例如网络异常)
            Log.d(TAG, "loadPicture - error: ${e?.toString()}")
            //printErrorMessage(e?.toString())
        }

        this.resultPicture = bitmapFromNetwork
        return resultPicture


    }

}