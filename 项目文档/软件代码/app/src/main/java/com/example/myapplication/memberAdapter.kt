package com.example.myapplication

import android.app.Activity
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import kotlinx.android.synthetic.main.comment_item2.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class memberAdapter (activity: Activity, val resourceId: Int, data:List<String>):
    ArrayAdapter<String>(activity, resourceId , data) {

    private lateinit var networkUtilsCoroutines: NetworkUtilsCoroutines

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(resourceId,parent,false)

        val name: TextView = view.findViewById(R.id.mem_name)
        val imageId: ImageView = view.findViewById(R.id.mem_head)
        val now = getItem(position)

        networkUtilsCoroutines = NetworkUtilsCoroutines(Bmob.getApplicationContext())

//        if(now!=null){
//            loadPictureCoroutines(now.img?.url.toString(),img)
//
//            name.setText(now.name)
//            comment.setText(now.comment)
//            time.setText(now.createAt)
//        }
//        return view

        if(now != null) {

            var bmobQuery: BmobQuery<User> = BmobQuery()
            bmobQuery.setLimit(500)
            bmobQuery.findObjects(object : FindListener<User>() {
                override fun done(users: MutableList<User>?, ex: BmobException?) {

                    if (ex == null) {
                        if (users != null) {
                            for (user: User in users) {
                                val str1 = user.getAccount().toString()
                                val flag1 = str1.equals(now)
                                if(flag1){
                                    name.setText(user.name)
                                    val icon: BmobFile? = user.getPicture()
                                    if(icon!=null)
                                        loadPictureCoroutines(icon.url.toString(),imageId)
                                    else imageId.setImageResource(R.drawable.start)

                                }
                            }
                        }
                    }
                }
            })
        }
        return view
    }

    fun setResult(bitmap: Bitmap?, img :ImageView) {
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