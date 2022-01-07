package com.example.myapplication

import android.app.Activity
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cn.bmob.v3.Bmob.getApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommentAdapter(activity: Activity, val resourceId:Int, data:List<Comment>):ArrayAdapter<Comment>(activity, resourceId , data) {

    //private lateinit var img:ImageView
    private lateinit var networkUtilsCoroutines: NetworkUtilsCoroutines

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(resourceId,parent,false)

        var img:ImageView= view.findViewById(R.id.image)
        val name:TextView = view.findViewById(R.id.name)
        val comment:TextView = view.findViewById(R.id.comment)
        val talk_time:TextView = view.findViewById(R.id.talk_time)

        val now = getItem(position)

        networkUtilsCoroutines = NetworkUtilsCoroutines(getApplicationContext())

        if(now!=null){


            loadPictureCoroutines(now.img?.url.toString(),img)

            name.setText(now.name)
            comment.setText(now.comment)
            talk_time.setText(now.createAt)
        }
        return view
    }

    fun setResult(bitmap: Bitmap?,img :ImageView) {
        if (bitmap != null) {
            //Toast.makeText(getApplicationContext(), "Load picture success!!!", Toast.LENGTH_SHORT).show()
            img.setImageBitmap(bitmap)

        } else {
            //Toast.makeText(getApplicationContext(), "Can not load picture !!!", Toast.LENGTH_SHORT).show()
            img.setImageResource(R.drawable.start)
        }
    }


    private fun loadPictureCoroutines(url:String,img :ImageView) {
        // 在主线程开启一个协程
        CoroutineScope(Dispatchers.Main).launch {
            // 切换到IO 线程 - withContext 能在指定IO 线程执行完成后，切换原来的线程
            var bitmap = withContext(Dispatchers.IO) {
                networkUtilsCoroutines.loadPicture(url)
            }
            // 切换了UI 线程，更新UI
            setResult(bitmap,img)
        }
    }
}