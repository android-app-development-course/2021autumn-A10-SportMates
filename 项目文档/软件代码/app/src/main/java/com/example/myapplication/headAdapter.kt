package com.example.myapplication

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class headAdapter(val headList: List<String>):
    RecyclerView.Adapter<headAdapter.ViewHolder>() {

    private lateinit var networkUtilsCoroutines: NetworkUtilsCoroutines

    inner class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val headImage: ImageView = view.findViewById(R.id.head_picture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.head, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        networkUtilsCoroutines = NetworkUtilsCoroutines(Bmob.getApplicationContext())

        val head = headList[position]
        //holder.headImage.setImageResource(R.drawable.start)

        if(head != null) {

            var bmobQuery: BmobQuery<User> = BmobQuery()
            bmobQuery.setLimit(500)
            bmobQuery.findObjects(object : FindListener<User>() {
                override fun done(users: MutableList<User>?, ex: BmobException?) {

                    if (ex == null) {
                        if (users != null) {
                            for (user: User in users) {
                                val str1 = user.getAccount().toString()
                                val flag1 = str1.equals(head)
                                if(flag1){
                                    val icon: BmobFile? = user.getPicture()
                                    if(icon!=null)
                                        loadPictureCoroutines(icon.url.toString(),holder.headImage)
                                    else holder.headImage.setImageResource(R.drawable.start)

                                }
                            }
                        }
                    }
                }
            })
        }

    }

    override fun getItemCount() = headList.size

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