package com.example.myapplication

import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.TextView
import android.widget.BaseAdapter
import android.util.SparseBooleanArray


class FriendsAdapter(context: Context, list: List<commodity>, resourceId: Int,selectMap : HashMap<Int,Boolean>) :
    BaseAdapter() {
    private val context: Context
    //定义数据源
    private val list: List<commodity>
    //定义布局资源Id
    private val resourceId: Int
    public var viewHolder: ViewHolder? = null
    private val selectMap: HashMap<Int,Boolean> = selectMap

    private val isShowCheckBox = false //表示当前是否是多选状态。


    override fun getCount(): Int {
        return list.size
    }
    // 获得某一位置的数据
    override fun getItem(position: Int): Any {
        return list[position]
    }
    //获得唯一标识
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View? {
        var view = view

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resourceId, null)
            viewHolder = ViewHolder()
            viewHolder!!.commo_image = view.findViewById<View>(R.id.commo_image) as ImageView
            viewHolder!!.commo_name = view.findViewById<View>(R.id.commo_name) as TextView

            viewHolder!!.commo_price = view.findViewById<View>(R.id.commo_price) as TextView
            viewHolder!!.commo_type = view.findViewById<View>(R.id.commo_type) as TextView
            viewHolder!!.commo_num = view.findViewById<View>(R.id.commo_num) as TextView

            viewHolder!!.addButton  = view.findViewById<View>(R.id.commo_add) as Button
            viewHolder!!.delButton  = view.findViewById<View>(R.id.commo_reduce) as Button

            viewHolder!!.checkBox = view.findViewById<View>(R.id.commo_check) as CheckBox

            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }
        viewHolder!!.commo_image!!.setImageResource(list[position].commo_image)
        viewHolder!!.commo_name!!.text = list[position].commo_name
        viewHolder!!.commo_price!!.text = "￥" + list[position].commo_price.toString()
        viewHolder!!.commo_type!!.text = list[position].commo_type
        viewHolder!!.commo_num!!.text = list[position].commo_num.toString()

        if(selectMap[position]==true)
            viewHolder!!.checkBox!!.setChecked(true)

        viewHolder!!.addButton!!.setOnClickListener(View.OnClickListener {
            mOnItemAddListener?.onAddClick(
                position
            )
        })

        viewHolder!!.delButton!!.setOnClickListener(View.OnClickListener {
            mOnItemDeleteListener?.onDeleteClick(
                position
            )
        })

        viewHolder!!.checkBox!!.setOnClickListener(View.OnClickListener {
            mOnItemSelectListener?.onSelectClick(
                position
            )
        })


        return view
    }

    private fun showAndHideCheckBox() {
        if (isShowCheckBox) {
            viewHolder!!.checkBox!!.visibility = View.VISIBLE
        } else {
            viewHolder!!.checkBox!!.visibility = View.GONE
        }
    }

    /*
         删除好友的监听接口
         */
    interface onItemDeleteListener {
        fun onDeleteClick(position: Int)
    }

    private var mOnItemDeleteListener: onItemDeleteListener? = null


    interface onItemAddListener{
        fun onAddClick(position: Int)
    }

    private var mOnItemAddListener: onItemAddListener? = null

    fun setOnItemDeleteClickListener(mOnItemDeleteListener: onItemDeleteListener?) {
        this.mOnItemDeleteListener = mOnItemDeleteListener
    }

    fun setOnItemAddClickListener(mOnItemAddListener: onItemAddListener?) {
        this.mOnItemAddListener = mOnItemAddListener

    }



    interface onItemSelectListener {
        fun onSelectClick(position: Int)
    }

    private var mOnItemSelectListener: onItemSelectListener? = null

    fun setOnItemSelectClickListener(mOnItemSelectListener: onItemSelectListener?) {
        this.mOnItemSelectListener = mOnItemSelectListener

    }

    inner class ViewHolder {
        var commo_image: ImageView? = null
        var commo_name: TextView? = null
        var commo_price: TextView? = null
        var commo_type: TextView? = null
        var addButton: Button?=null
        var delButton: Button?=null
        var commo_num: TextView? = null

        //var mTvData: TextView? = null
        var checkBox: CheckBox? = null
    }

    init {
        this.context = context
        this.list = list
        this.resourceId = resourceId
    }
}
