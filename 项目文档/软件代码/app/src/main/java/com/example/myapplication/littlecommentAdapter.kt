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
import cn.bmob.v3.Bmob
import kotlinx.android.synthetic.main.comment_item2.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class littlecommentAdapter(activity: Activity, val resourceId:Int, data: ArrayList<littlecomment>):
    ArrayAdapter<littlecomment>(activity, resourceId , data) {

    private lateinit var networkUtilsCoroutines: NetworkUtilsCoroutines

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(resourceId,parent,false)
        var img: ImageView = view.findViewById(R.id.image_)
        val name: TextView = view.findViewById(R.id.name_)
        val comment: TextView = view.findViewById(R.id.comment_)
        val time:TextView = view.findViewById(R.id.time_)
        val now = getItem(position)

        networkUtilsCoroutines = NetworkUtilsCoroutines(Bmob.getApplicationContext())

        if(now!=null){
            loadPictureCoroutines(now.img?.url.toString(),img)

            name.setText(now.name)
            comment.setText(now.comment)
            time.setText(now.createAt)
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
        // ??????????????????????????????
        CoroutineScope(Dispatchers.Main).launch {
            // ?????????IO ?????? - withContext ????????????IO ?????????????????????????????????????????????
            var bitmap = withContext(Dispatchers.IO) {
                networkUtilsCoroutines.loadPicture(url)
            }
            // ?????????UI ???????????????UI
            setResult(bitmap,img)
        }
    }
}