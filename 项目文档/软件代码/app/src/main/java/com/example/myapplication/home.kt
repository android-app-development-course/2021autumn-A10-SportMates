package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobQueryResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SQLQueryListener
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.refresh
import kotlinx.android.synthetic.main.activity_mycomments.*
import kotlinx.android.synthetic.main.bar.*
import org.json.JSONArray
import java.lang.Exception
import java.util.ArrayList
import kotlin.concurrent.thread

/*
首页home.kt

定位
获取login的账号pwd
根据定位的位置显示附近的队伍
*/

class home : AppCompatActivity() {
    private val teamList = ArrayList<Team>()
    private lateinit var useraccount:String

//    private ArrayList<JsonBean> options1Items = new ArrayList<>(); //省
//    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();//市
//    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();//区

//    private var options1Items = ArrayList<JsonBean>() //省
//    private val options2Items = ArrayList<ArrayList<String>>() //市
//    private val options3Items = ArrayList<ArrayList<ArrayList<String>>>() //区

    private var mLocationClient: LocationClient? = null

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        home.setImageResource(R.drawable.home2)
        hometext.setTextColor(Color.parseColor("#68B2A0"))

        useraccount = intent.getStringExtra("account").toString()

        /*
        TextView now_place显示当前位置，点击定位图标可手动选择位置
        */

        val provinceItems = mutableListOf<AddressInfoPO>()
        val cityItems = mutableListOf<MutableList<AddressInfoPO>>()
        val areaItems = mutableListOf<MutableList<MutableList<AddressInfoPO>>>()
        //Json2Bean
        val pcaCodeList = Gson().fromJson<MutableList<PCACodePO>>(FileUtil
            .getAssetsFileText(this, "pca-code.json"),
            object : TypeToken<MutableList<PCACodePO>>() {}.type)
        //遍历省
        pcaCodeList.forEach {pcaCode ->
            //存放省内市区
            val cityList= mutableListOf<AddressInfoPO>()
            //存放省内所有辖区
            val areaList= mutableListOf<MutableList<AddressInfoPO>>()
            //遍历省内市区
            pcaCode.children.forEach { cCode ->
                //添加省内市区
                cityList.add(AddressInfoPO(cCode.code,cCode.name))
                //存放市内辖区
                val areas= mutableListOf<AddressInfoPO>()
                //添加市内辖区
                cCode.children.forEach {addressInfo->
                    areas.add(addressInfo)
                }
                areaList.add(areas)
            }
            //添加省份
            provinceItems.add(AddressInfoPO(pcaCode.code,pcaCode.name))
            //添加市区
            cityItems.add(cityList)
            //添加辖区
            areaItems.add(areaList)
        }
        //显示选择器
        now_place.setOnClickListener{
            showAddressPicker(provinceItems,cityItems,areaItems)
        }

        p1.setOnClickListener{
            showAddressPicker(provinceItems,cityItems,areaItems)
        }

        get_place()

//        initTeam()
        val adapter = TeamAdapter(this, R.layout.team, teamList)
        listView_team.adapter = adapter

        add.setOnClickListener {
            val intent = Intent(this, new_team::class.java)
            intent.putExtra("account",useraccount)
            startActivityForResult(intent, 1)
        }

        listView_team.setOnItemClickListener { parent, view, position, id ->
            val now = teamList[position]
            if (now.leader == useraccount) {
                val intent = Intent(this, leader_view::class.java)
                intent.putExtra("team_object_id", now.id)
                startActivityForResult(intent, 1)
            } else {
                val intent = Intent(this, team_details::class.java)
                intent.putExtra("team_object_id", now.id)
                intent.putExtra("account",useraccount)
                startActivityForResult(intent, 1)
            }
        }

        mine.setOnClickListener {
            val intent = Intent(this,com.example.myapplication.mine::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }

        talk.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }

        shop_.setOnClickListener {
            val intent = Intent(this, shop::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }



        val isRefreshing = false //listview是否可用

        listView_team.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                //判断listview第一个可见的条目是否是第一个条目
                if (listView_team.firstVisiblePosition == 0) {
                    val firstVisibleItemView: View = listView_team.getChildAt(0)
                    //判断第一个条目距离listview是否是0,如果是，则srLayout可用，否则不可用
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() === 0) {
                        refresh.setEnabled(true)
                    } else {
                        refresh.setEnabled(false)
                    }
                } else {
                    refresh.setEnabled(false)
                }
                //根据当前是否是在刷新数据，来决定是否拦截listview的触摸事件
                return isRefreshing
            }
        })



        refresh.setOnRefreshListener {

            thread {
                Thread.sleep(2000)
                runOnUiThread{
                    initTeam()

                    refresh.isRefreshing = false
                }
            }

        }
    }

    /**
     * 显示地址选择
     */
    fun showAddressPicker(provinceItems: MutableList<AddressInfoPO>,
                          cityItems: MutableList<MutableList<AddressInfoPO>>,
                          areaItems: MutableList<MutableList<MutableList<AddressInfoPO>>>) {
        val addressPv =
            OptionsPickerBuilder(this, OnOptionsSelectListener { options1, options2, options3, v ->
                //省份
                provinceItems[options1]
                //城市
                cityItems[options1][options2]
                //辖区
                areaItems[options1][options2][options3]
                //获得选择的数据
//                var  tx:String= provinceItems.get(options1).pickerViewText+cityItems.get(options1).get(options2).pickerViewText+areaItems.get(options1).get(options2).get(options3).pickerViewText
                var  tx:String= cityItems.get(options1).get(options2).pickerViewText+areaItems.get(options1).get(options2).get(options3).pickerViewText

                now_place.setText(tx)
                initTeam()

            })
                // .setTitleText(pickerEnum.title)
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build<AddressInfoPO>()
        addressPv.setPicker(provinceItems, cityItems, areaItems)
        addressPv.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            initTeam()
        }
    }

    private fun initTeam() {
        val tx = now_place.text

        val bql = "Select * from team_table"
        var bmobQuery: BmobQuery<team_table> = BmobQuery()
        teamList.clear()
        bmobQuery.setLimit(500)
        bmobQuery.doSQLQuery(bql, object : SQLQueryListener<team_table>() {
            override fun done(teams: BmobQueryResult<team_table>?, ex: BmobException?) {
//                val teamList = ArrayList<Team>()
                if (ex == null) {
                    if (teams != null) {
                        for (t: team_table in teams.getResults()) {
                            val n1 = t.name.toString()
                            val p1 = t.place.toString()
                            val t1 = t.time.toString()
                            val t2 = t.type.toString()
                            val n2 = t.number.toString()
                            val i = t.objectId.toString()
                            val l = t.leader.toString()
                            val mem = t.member.toString()
                            var len = 0
                            if (mem != "") {
                                len = mem.split(";").size-1
                            }

                            if (p1.contains(tx)) {
                                teamList.add(Team(n1, p1, t1, t2, (len+1).toString() + "/" + n2, l, i))
                            }

                            val adapter = TeamAdapter(this@home, R.layout.team, teamList)
                            listView_team.adapter = adapter
                        }
                    }
                } else {
                    Toast.makeText(this@home, ex.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    inner class MyLocationListener : BDAbstractLocationListener() {
        var addr: String? = null
        var country: String? = null
        var province: String? = null
        var city: String? = null
        var district: String? = null
        var street: String? = null
        var adcode: String? = null
        var town: String? = null
        override fun onReceiveLocation(location: BDLocation) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            addr = location.addrStr //获取详细地址信息
            country = location.country //获取国家
            province = location.province //获取省份
            city = location.city //获取城市
            district = location.district //获取区县
            street = location.street //获取街道信息
            adcode = location.adCode //获取adcode
            town = location.town //获取乡镇信息

            now_place.text = city + district
            initTeam()

//            Toast.makeText(getApplicationContext(), addr, Toast.LENGTH_LONG).show()

        }
    }

    fun get_place() {
        val myListener = MyLocationListener()
        mLocationClient = LocationClient(getApplicationContext())
        //声明LocationClient类
        mLocationClient?.registerLocationListener(myListener)
        //注册监听函数

        val option = LocationClientOption()

        option.setIsNeedAddress(true);
//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true

        option.setNeedNewVersionRgc(true);
//可选，设置是否需要最新版本的地址信息。默认需要，即参数为true

        mLocationClient?.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        mLocationClient!!.start()


//        Toast.makeText(this,"888", Toast.LENGTH_LONG).show()
        //Toast.makeText(this, myListener.addr, Toast.LENGTH_LONG).show()
    }

    /**
     * 显示地址选择
     */




}

/*
密码：android

密钥库类型: PKCS12
密钥库提供方: SUN

您的密钥库包含 1 个条目

别名: androiddebugkey
创建日期: 2021-12-25
条目类型: PrivateKeyEntry
证书链长度: 1
证书[1]:
所有者: CN=lcy, OU=scnu, O=scnu, L=Unknown, ST=Unknown, C=Unknown
发布者: CN=lcy, OU=scnu, O=scnu, L=Unknown, ST=Unknown, C=Unknown
序列号: 3efa8dfa
生效时间: Sat Dec 25 17:34:21 GMT+08:00 2021, 失效时间: Wed May 12 17:34:21 GMT+08:00 2049
证书指纹:
         SHA1: C6:79:AC:DC:9F:B9:B1:60:2E:E8:BD:08:F8:BB:63:DF:99:9F:7E:C8
         SHA256: 4A:6A:29:DB:15:20:A7:4D:4E:7A:EB:8E:4F:9F:D8:8C:50:45:52:BB:E9:8B:31:82:D1:CB:1B:B3:3E:94:D2:46
签名算法名称: SHA256withRSA
主体公共密钥算法: 2048 位 RSA 密钥
版本: 3

扩展:

#1: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: 36 76 82 C7 93 EE 9B 12   72 23 C8 C0 CB D8 6D F2  6v......r#....m.
0010: D9 59 33 6F                                        .Y3o
]
]



*******************************************
*******************************************

 */

/*
百度地图逆地理编码
https://api.map.baidu.com/reverse_geocoding/v3/?ak=IzbU3Sd1iRNOZ9hc06UjncFoNxdd9jDT&mcode=C6:79:AC:DC:9F:B9:B1:60:2E:E8:BD:08:F8:BB:63:DF:99:9F:7E:C8;com.example.myapplication%20&output=json&coordtype=wgs84ll&location=31.225696563611,121.49884033194
 */