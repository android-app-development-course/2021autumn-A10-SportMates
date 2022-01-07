package com.example.myapplication

import android.Manifest
import android.R.attr
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_modify_mydata.*
import kotlinx.android.synthetic.main.activity_modify_mydata.account
import cn.bmob.v3.listener.UploadFileListener

import cn.bmob.v3.datatype.BmobFile
import java.io.File
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.UriUtils.getFileAbsolutePath
import kotlinx.android.synthetic.main.activity_mine.*
import kotlinx.android.synthetic.main.activity_modify_mydata.name
import kotlinx.android.synthetic.main.activity_modify_mydata.times
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class modify_Mydata : AppCompatActivity() {
    private lateinit var useraccount:String
    private lateinit var objID:String
    private lateinit var userpassword:String
    private lateinit var networkUtilsCoroutines: NetworkUtilsCoroutines
    val fromAlbum = 2

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_mydata)
        supportActionBar?.hide()

        useraccount = intent.getStringExtra("account").toString()
        account.setText(useraccount)

        networkUtilsCoroutines = NetworkUtilsCoroutines(this)

        var bmobQuery: BmobQuery<User> = BmobQuery()
        bmobQuery.setLimit(500)
        bmobQuery.findObjects(object : FindListener<User>() {
            override fun done(users: MutableList<User>?, ex: BmobException?) {

                if (ex == null) {
                    if (users != null) {
                        for (user: User in users) {
                            val str1 = user.getAccount().toString()
                            val flag1 = str1.equals(useraccount)
                            if(flag1){
                                times.setText(user.getTimes().toString())

                                if(user.getSex().equals("男"))
                                    radiobutton1.setChecked(true)
                                else if(user.getSex().equals("女"))
                                    radiobutton2.setChecked(true)

                                name.setText(user.getName().toString())

                                objID = user.objectId
                                userpassword = user.password.toString()

                                val icon: BmobFile? = user.getPicture()
                                if(icon!=null)
                                    loadPictureCoroutines(icon.url.toString())

                            }
                        }
                    }
                }
            }
        })


        store.setOnClickListener{
            updateObject(objID)
        }

        image.setOnClickListener{
            AlertDialog.Builder(this).apply {
                setTitle("更换头像")
                setMessage("请问您是否需要更换头像？")
                setCancelable(false)
                setPositiveButton("是"){dialog,which->
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)

                    intent.type = "image/*"
                    startActivityForResult(intent,fromAlbum)

                    Toast.makeText(this@modify_Mydata, "更换头像成功！", Toast.LENGTH_LONG).show()
                }
                setNegativeButton("否"){dialog,which->

                }
                show()
            }
        }
    }

    private fun updateObject(objectId: String?) {
        val user = User()

        user.account = useraccount

        if(radiobutton1.isChecked)
            user.sex = "男"
        else if(radiobutton2.isChecked)
            user.sex = "女"

        user.times = times.text.toString().toInt()

        user.name = name.text.toString()

        val psw = password.text.toString()
        if(psw.length>=6)
            user.password = psw
        else if(psw.length == 0){
            user.password = userpassword
        }
        else {
            user.password = userpassword
            Toast.makeText(
                this@modify_Mydata,
                "请输入大于等于6位的密码,密码修改失败",
                Toast.LENGTH_LONG
            ).show()
        }

        user.objectId = objID

        user.update(object : UpdateListener() {
            override fun done(ex: BmobException?) {
                if (ex == null) {
                    Toast.makeText(this@modify_Mydata, "保存成功！", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@modify_Mydata, ex.message, Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            fromAlbum->{

                if (ContextCompat.checkSelfPermission(this,  Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                }

                val uri = data?.data

                //大牛的转换函数，特别棒！
                val path = getFileAbsolutePath(this, uri);


                if(resultCode == Activity.RESULT_OK && data!=null){
                    data.data?.let { uri->
                        val bitmap = getBitmapFromUri(uri)
                        image.setImageBitmap(bitmap)

                        uploadSingle(path)
                    }
                }

            }
        }
    }

    private fun getBitmapFromUri(uri:Uri) = contentResolver.openFileDescriptor(uri,"r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }



    private fun setFileToTable(bmobFile: BmobFile) {
        var user = User()
        user.picture = bmobFile

        user.objectId = objID

        user.update(object : UpdateListener() {
            override fun done(ex: BmobException?) {
                if (ex == null) {
                    //Toast.makeText(this@modify_Mydata, "成功将文件设置到表中", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@modify_Mydata, ex.message, Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun uploadSingle(path: String?) {
        var file = File(path)
        var bmobFile = BmobFile(file)
        bmobFile.upload(object : UploadFileListener() {
            override fun done(ex: BmobException?) {
                if (ex == null) {
                    //Toast.makeText(this@modify_Mydata, "上传成功", Toast.LENGTH_SHORT).show()
                    setFileToTable(bmobFile)
                } else {
                    Toast.makeText(this@modify_Mydata, "上传失败："+ex.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun setResult(bitmap: Bitmap?) {
        if (bitmap != null) {
            //Toast.makeText(this, "Load picture success!!!", Toast.LENGTH_SHORT).show()
            image.setImageBitmap(bitmap)
        } else {
            Toast.makeText(this, "Can not load picture !!!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun loadPictureCoroutines(url:String) {
        // 在主线程开启一个协程
        CoroutineScope(Dispatchers.Main).launch {
            // 切换到IO 线程 - withContext 能在指定IO 线程执行完成后，切换原来的线程
            var bitmap = withContext(Dispatchers.IO) {
                networkUtilsCoroutines.loadPicture(url)
            }
            // 切换了UI 线程，更新UI
            setResult(bitmap)
        }
    }
}