package com.example.myapplication

import android.R
import android.widget.TextView
import android.widget.Toast

import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable
import java.util.ArrayList


class commodity (val commo_name:String,val commo_image:Int, val commo_price:Double,var commo_num :Int,val commo_type:String):
    Serializable{
    private val serialVersionUID = 3L
}




//class MainActivity : AppCompatActivity(), View.OnClickListener {
//    private var lvData: ListView? = null
//    private var mLlEditBar //控制下方那一行的显示与隐藏
//            : LinearLayout? = null
//    private var adapter: MyAdapter? = null
//    private val mData: MutableList<String> = ArrayList() //所有数据
//    private val mCheckedData: MutableList<String> = ArrayList() //将选中数据放入里面
//    private val stateCheckedMap = SparseBooleanArray() //用来存放CheckBox的选中状态，true为选中,false为没有选中
//    private var isSelectedAll = true //用来控制点击全选，全选和全不选相互切换
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        initView()
//        initData()
//        adapter = MyAdapter(this@MainActivity, mData, stateCheckedMap)
//        lvData!!.adapter = adapter
//        setOnListViewItemClickListener()
//        setOnListViewItemLongClickListener()
//    }
//
//    override fun onClick(v: View) {
//        when (v.id) {
//            R.id.ll_cancel -> cancel()
//            R.id.ll_delete -> delete()
//            R.id.ll_inverse -> inverse()
//            R.id.ll_select_all -> selectAll()
//        }
//    }
//
//    private fun cancel() {
//        setStateCheckedMap(false) //将CheckBox的所有选中状态变成未选中
//        mLlEditBar!!.visibility = View.GONE //隐藏下方布局
//        adapter.setShowCheckBox(false) //让CheckBox那个方框隐藏
//        adapter.notifyDataSetChanged() //更新ListView
//    }
//
//    private fun delete() {
//        if (mCheckedData.size == 0) {
//            Toast.makeText(this@MainActivity, "您还没有选中任何数据！", Toast.LENGTH_SHORT).show()
//            return
//        }
//        else beSureDelete()
////        val dialog = CustomDialog(this)
////        dialog.show()
////        dialog.setHintText("是否删除？")
////        dialog.setLeftButton("取消", View.OnClickListener { dialog.dismiss() })
////        dialog.setRightButton("确定", View.OnClickListener {
////            beSureDelete()
////            dialog.dismiss()
////        })
//    }
//
//    private fun beSureDelete() {
//        mData.removeAll(mCheckedData) //删除选中数据
//        setStateCheckedMap(false) //将CheckBox的所有选中状态变成未选中
//        mCheckedData.clear() //清空选中数据
//        adapter.notifyDataSetChanged()
//        Toast.makeText(this@MainActivity, "删除成功", Toast.LENGTH_SHORT).show()
//    }
//
//    /**
//     * 反选就是stateCheckedMap的值为true时变为false,false时变成true
//     */
//    private fun inverse() {
//        mCheckedData.clear()
//        for (i in mData.indices) {
//            if (stateCheckedMap[i]) {
//                stateCheckedMap.put(i, false)
//            } else {
//                stateCheckedMap.put(i, true)
//                mCheckedData.add(mData[i])
//            }
//            lvData!!.setItemChecked(i, stateCheckedMap[i]) //这个好行可以控制ListView复用的问题，不设置这个会出现点击一个会选中多个
//        }
//        adapter.notifyDataSetChanged()
//    }
//
//    private fun selectAll() {
//        mCheckedData.clear() //清空之前选中数据
//        if (isSelectedAll) {
//            setStateCheckedMap(true) //将CheckBox的所有选中状态变成选中
//            isSelectedAll = false
//            mCheckedData.addAll(mData) //把所有的数据添加到选中列表中
//        } else {
//            setStateCheckedMap(false) //将CheckBox的所有选中状态变成未选中
//            isSelectedAll = true
//        }
//        adapter.notifyDataSetChanged()
//    }
//
//    private fun setOnListViewItemClickListener() {
//        lvData!!.onItemClickListener =
//            OnItemClickListener { parent, view, position, id ->
//                updateCheckBoxStatus(
//                    view,
//                    position
//                )
//            }
//    }
//
//    /**
//     * 如果返回false那么click仍然会被调用,,先调用Long click，然后调用click。
//     * 如果返回true那么click就会被吃掉，click就不会再被调用了
//     * 在这里click即setOnItemClickListener
//     */
//    private fun setOnListViewItemLongClickListener() {
//        lvData!!.onItemLongClickListener =
//            OnItemLongClickListener { parent, view, position, id ->
//                mLlEditBar!!.visibility = View.VISIBLE //显示下方布局
//                adapter.setShowCheckBox(true) //CheckBox的那个方框显示
//                updateCheckBoxStatus(view, position)
//                true
//            }
//    }
//
//    private fun updateCheckBoxStatus(view: View, position: Int) {
//        val holder: MyAdapter.ViewHolder = view.tag as MyAdapter.ViewHolder
//        holder.checkBox.toggle() //反转CheckBox的选中状态
//        lvData!!.setItemChecked(position, holder.checkBox.isChecked()) //长按ListView时选中按的那一项
//        stateCheckedMap.put(position, holder.checkBox.isChecked()) //存放CheckBox的选中状态
//        if (holder.checkBox.isChecked()) {
//            mCheckedData.add(mData[position]) //CheckBox选中时，把这一项的数据加到选中数据列表
//        } else {
//            mCheckedData.remove(mData[position]) //CheckBox未选中时，把这一项的数据从选中数据列表移除
//        }
//        adapter.notifyDataSetChanged()
//    }
//
//    private fun initView() {
//        lvData = findViewById<View>(R.id.lv) as ListView
//        mLlEditBar = findViewById(R.id.ll_edit_bar)
//        findViewById<View>(R.id.ll_cancel).setOnClickListener(this)
//        findViewById<View>(R.id.ll_delete).setOnClickListener(this)
//        findViewById<View>(R.id.ll_inverse).setOnClickListener(this)
//        findViewById<View>(R.id.ll_select_all).setOnClickListener(this)
//        lvData!!.choiceMode = ListView.CHOICE_MODE_MULTIPLE
//    }
//
//    private fun initData() {
//        for (i in 0..999) {
//            mData.add("第" + i + "项")
//        }
//        setStateCheckedMap(false)
//    }
//
//    /**
//     * 设置所有CheckBox的选中状态
//     */
//    private fun setStateCheckedMap(isSelectedAll: Boolean) {
//        for (i in mData.indices) {
//            stateCheckedMap.put(i, isSelectedAll)
//            lvData!!.setItemChecked(i, isSelectedAll)
//        }
//    }
//
//    override fun onBackPressed() {
//        if (mLlEditBar!!.visibility == View.VISIBLE) {
//            cancel()
//            return
//        }
//        super.onBackPressed()
//    }
//}




/**
 * Created by yangms on 2018/7/16.
 */
//class MyAdapter(context: Context, data: List<String>, stateCheckedMap: SparseBooleanArray) :
//    BaseAdapter() {
//    var data: List<String> = ArrayList()
//    private val mContext: Context
//    var holder: ViewHolder? = null
//    var isShowCheckBox = false //表示当前是否是多选状态。
//    private val stateCheckedMap = SparseBooleanArray() //用来存放CheckBox的选中状态，true为选中,false为没有选中
//    override fun getCount(): Int {
//        return data.size
//    }
//
//    override fun getItem(i: Int): Any {
//        return null
//    }
//
//    override fun getItemId(i: Int): Long {
//        return i.toLong()
//    }
//
//    override fun getView(position: Int, convertView: View, viewGroup: ViewGroup): View {
//        var convertView = convertView
//        if (convertView == null) {
//            holder = ViewHolder()
//            convertView = View.inflate(mContext, R.layout.item, null)
//            convertView.tag = holder
//        } else {
//            holder = convertView.tag as ViewHolder
//        }
//        holder!!.checkBox =
//            convertView.findViewById<View>(R.id.chb_select_way_point) as AppCompatCheckBox
//        holder!!.mTvData = convertView.findViewById(R.id.tv_data)
//        showAndHideCheckBox() //控制CheckBox的那个的框显示与隐藏
//        holder!!.mTvData.setText(data[position])
//        holder!!.checkBox!!.isChecked = stateCheckedMap[position] //设置CheckBox是否选中
//        return convertView
//    }
//
//    inner class ViewHolder {
//        var mTvData: TextView? = null
//        var checkBox: AppCompatCheckBox? = null
//    }
//
//    private fun showAndHideCheckBox() {
//        if (isShowCheckBox) {
//            holder!!.checkBox!!.visibility = View.VISIBLE
//        } else {
//            holder!!.checkBox!!.visibility = View.GONE
//        }
//    }
//
//    init {
//        this.data = data
//        mContext = context
//        this.stateCheckedMap = stateCheckedMap
//    }
//}
//
//class MainActivity : AppCompatActivity() {
//    private var adapter: MyAdapter? = null
//    private var textView: TextView? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        val listView: ListView = findViewById(R.id.lv_list)
//        val costBeanList: MutableList<CostBean> = ArrayList<CostBean>() //创建封装好的bean对象
//
//        //	装配数据源
//        costBeanList.add(CostBean(R.drawable.ic_one, "狐狸", "聪明的狐狸"))
//        costBeanList.add(CostBean(R.drawable.ic_kenan, "玫瑰", "带刺的花儿"))
//        costBeanList.add(CostBean(R.drawable.ic_three, "国王", "没有一个臣民"))
//        adapter = MyAdapter(this, costBeanList)
//
//        //  实现回调接口
//        adapter.setXmOnItemDeleteListener(object : onItemDeleteListener() {
//            //  当用户点击了收藏按钮时,系统就会自动调用里面的方法
//            fun onDeleteClick(position: Int) {                       //  position  是Item的位置
//                val view = findView(position, listView) //  根据 position的下标 与 listView找到他的 View
//                textView = view.findViewById(R.id.tv_name)
//                val str = textView.getText().toString()
//                Toast.makeText(this@MainActivity, "$str\n第 $position 项", Toast.LENGTH_LONG).show()
//            }
//        })
//        listView.setAdapter(adapter)
//    }
//
//    //  得到每一个不同的Item所对应的 View
//    private fun findView(position: Int, listView: ListView): View {
//        val firstListItemPosition: Int = listView.getFirstVisiblePosition()
//        +listView.getChildCount() - 1
//        return if (position < firstListItemPosition || position > firstListItemPosition) {
//            listView.getAdapter().getView(position, null, listView)
//        } else {
//            val childIndex = position - firstListItemPosition
//            listView.getChildAt(childIndex)
//        }
//    }
//}

