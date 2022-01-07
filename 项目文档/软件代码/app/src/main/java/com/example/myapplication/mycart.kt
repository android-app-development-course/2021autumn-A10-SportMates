package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.baidu.mapapi.BMapManager.getContext
import kotlinx.android.synthetic.main.activity_mycart.*
import kotlinx.android.synthetic.main.each_commod.*
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.util.set
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import kotlinx.android.synthetic.main.activity_goods_detail.*
import java.math.RoundingMode
import java.text.DecimalFormat


class mycart : AppCompatActivity() {
    var cartList = ArrayList<commodity>() //全部数据
    private val mCheckedData = ArrayList<commodity>() //将选中数据放入里面
    private var adapter: FriendsAdapter? = null
    private val mCheckedDataBuy = ArrayList<commodity>() //将选中数据放入里面
    private var selectMap = HashMap<Int,Boolean>()

    private lateinit var useraccount:String

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mycart)
        supportActionBar?.hide()

        useraccount = intent.getStringExtra("account").toString()

        val DBhelper = MyDatabaseHelper(this,"Cart.db",1)

        val db = DBhelper.writableDatabase

        val cursor = db.query("Cart", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据并打印
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val imgid  = cursor.getInt(cursor.getColumnIndex("image"))
                val price = cursor.getString(cursor.getColumnIndex("price"))
                val num = cursor.getInt(cursor.getColumnIndex("num"))

                cartList.add(commodity(name,imgid,price.toDouble(),num,"官方标配"))

            } while (cursor.moveToNext())
        }
        cursor.close()



        for(i in 0..cartList.size-1)
            selectMap[i] = false

        adapter = getContext()?.let {
            FriendsAdapter(
                it, cartList,
                R.layout.each_commod,selectMap)
        }
        cart_goods!!.adapter = adapter

        //减少数量
        adapter!!.setOnItemDeleteClickListener(object : FriendsAdapter.onItemDeleteListener {
            override fun onDeleteClick(i: Int) {
                val VIEW = findView(i,cart_goods)
                val num :TextView  = VIEW.findViewById<View>(R.id.commo_num) as TextView

                val num_ = num.text.toString().toInt()
                if(num_>1){
                    cartList[i].commo_num = num_-1
                }

                cart_goods!!.adapter = adapter

                cal()
            }
        })

        //增加数量
        adapter!!.setOnItemAddClickListener(object : FriendsAdapter.onItemAddListener {
            override fun onAddClick(i: Int) {
                val VIEW = findView(i,cart_goods)
                val num :TextView  = VIEW.findViewById<View>(R.id.commo_num) as TextView

                val num_ = num.text.toString().toInt()

                cartList[i].commo_num = num_+1

                cart_goods!!.adapter = adapter

                cal()

            }
        })


        adapter!!.setOnItemSelectClickListener(object : FriendsAdapter.onItemSelectListener {
            override fun onSelectClick(i: Int) {
                val VIEW = findView(i,cart_goods)
                val checkBox :CheckBox  = VIEW.findViewById<View>(R.id.commo_check) as CheckBox

                if(checkBox.isChecked&&i!=0){
                    checkBox.setChecked(false)
                    selectMap[i] = false

               }
                else if(!checkBox.isChecked&&i!=0){
                    checkBox.setChecked(true)
                    selectMap[i] = true

                }

                if(i==0){
                    if(selectMap[i] == false)
                        selectMap[i] = true
                    else
                        selectMap[i] = false
                }
                cal()

            }
        })

        cart_all_choose.setOnCheckedChangeListener {  compoundButton, b ->
            if (cart_all_choose.isChecked) {
                for (i in 0..cartList.size-1)
                    selectMap[i] = true
            } else {
                for (i in 0..cartList.size-1)
                    selectMap[i] = false
            }
            cart_goods!!.adapter = adapter

            cal()
        }

        cart_delete.setOnClickListener {
            delete()
        }

        //结算
        cart_check_out.setOnClickListener {
            mCheckedDataBuy.clear()
            for( i in 0..selectMap.size-1){
                if(selectMap[i] == true){
                    mCheckedDataBuy.add(cartList[i])
                }
            }

            val intent = Intent(this, create_order::class.java)
            val bundle = Bundle()
            bundle.putSerializable("buy_now", mCheckedDataBuy)
            intent.putExtras(bundle)
            intent.putExtra("account",useraccount)
            startActivityForResult(intent,1)


        }

    }

    @SuppressLint("Range")
    private fun delete() {
        for( i in 0..selectMap.size-1){
            if(selectMap[i] == true){
                mCheckedData.add(cartList[i])
            }
        }

        if (mCheckedData.isEmpty()) {
            Toast.makeText(this, "您还没有选中任何数据！", Toast.LENGTH_SHORT).show()
            return
        }
        else {
            for(i in mCheckedData){
                val DBhelper = MyDatabaseHelper(this,"Cart.db",1)

                val db = DBhelper.writableDatabase

                val cursor = db.query("Cart", null, null, null, null, null, null)
                if (cursor.moveToFirst()) {
                    do {
                        // 遍历Cursor对象，取出数据并打印
                        val name = cursor.getString(cursor.getColumnIndex("name"))
                        if(name.equals(i.commo_name)) {
                            db.execSQL("delete from Cart where name = ?", arrayOf(name))
                            break
                        }

                    } while (cursor.moveToNext())
                }
                cursor.close()
            }

            cartList.removeAll(mCheckedData) //删除选中数据
            mCheckedData.clear() //清空选中数据

            selectMap.clear()
            for(i in 0..cartList.size-1)
                selectMap[i] = false

            adapter?.notifyDataSetChanged()
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show()

            cart_goods!!.adapter = adapter

        }
    }

    private fun findView(position: Int, listView: ListView): View {
        val firstListItemPosition: Int = listView.getFirstVisiblePosition()
        +listView.getChildCount() - 1
        return if (position < firstListItemPosition || position > firstListItemPosition) {
            listView.getAdapter().getView(position, null, listView)
        } else {
            val childIndex = position - firstListItemPosition
            listView.getChildAt(childIndex)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1-> if(resultCode == RESULT_OK){
                delete()
//                cart_price_sum.setText("0")
//                cart_total_num.setText("0")
                cal()
            }
        }
    }

    fun cal(){
        var num = 0
        var total = 0.0
        for(i in 0..selectMap.size-1){
            if(selectMap[i]==true){
                num = num+cartList[i].commo_num
                total = total+cartList[i].commo_num*cartList[i].commo_price
            }
        }

        val format = DecimalFormat("0.#")
        //未保留小数的舍弃规则，RoundingMode.FLOOR表示直接舍弃。
        format.roundingMode = RoundingMode.FLOOR



        cart_total_num.setText(num.toString())
        cart_price_sum.setText("¥"+format.format(total))
    }
}
