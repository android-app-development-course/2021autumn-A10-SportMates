package com.example.myapplication

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.baidu.mapapi.SDKInitializer

import android.app.Application
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.bmob.v3.Bmob.getApplicationContext
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng


class map : AppCompatActivity() {
    private var mMapView: MapView? = null
    private var mBaiduMap: BaiduMap? = null
    private var mLocationClient: LocationClient? = null
    private var mMarker: BitmapDescriptor? = null
    private var ifFrist = true

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        if (ContextCompat.checkSelfPermission(this,  Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
//            showmap()
//            ddd()

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    showmap()
//                    ddd()

                } else {

                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
//
//    override fun onResume() {
//        super.onResume()
//        mMapView!!.onResume()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        mMapView!!.onPause()
//    }
//
//    override fun onDestroy() {
//        mLocationClient!!.stop()
//        mMapView!!.onDestroy()
//        //mMapView = null;
//        super.onDestroy()
//    }
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

//            Toast.makeText(getApplicationContext(), addr, Toast.LENGTH_LONG).show()

        }
    }
//
//    inner class MyLocationListener : BDAbstractLocationListener() {
//        override fun onReceiveLocation(location: BDLocation) {
//            //mapView 销毁后不在处理新接收的位置
//            if (location == null || mMapView == null) {
//                return
//            }
//            val locData = MyLocationData.Builder()
//                .accuracy(location.radius) // 此处设置开发者获取到的方向信息，顺时针0-360
//                .latitude(location.latitude)
//                .longitude(location.longitude)
//                .direction(location.direction)
//                .build()
//            mBaiduMap!!.setMyLocationData(locData)
//            val configuration = MyLocationConfiguration(
//                MyLocationConfiguration.LocationMode.NORMAL, false, mMarker
//            )
//            mBaiduMap!!.setMyLocationConfiguration(configuration)
//            if (ifFrist) {
//                val ll = LatLng(location.latitude, location.longitude)
//                val builder = MapStatus.Builder()
//                builder.target(ll)
//                builder.zoom(18.0f)
//                mBaiduMap!!.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
//                //放大层级
//                ifFrist = false
//            }
//        }
//    }

//    fun showmap(){
//        mMapView = findViewById(R.id.bmapView)
//        mBaiduMap = mMapView?.getMap()
//        mLocationClient = LocationClient(this)
//        val myLocationListener: com.example.myapplication.MyLocationListener = com.example.myapplication.MyLocationListener()
//        //val myLocationListener: MyLocationListener = MyLocationListener()
//        mLocationClient!!.registerLocationListener(myLocationListener)
//
//        //覆盖物 用于显示当前位置
//        mMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_background)
//        mBaiduMap?.setMyLocationEnabled(true)
//        val option = LocationClientOption()
//        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
//        option.isOpenGps = true // 打开gps
//        option.setCoorType("bd09ll") // 设置坐标类型
//        //option.setNeedNewVersionRgc(true)
//        option.setScanSpan(1000)
//        option.setIsNeedAddress(true)
//        mLocationClient!!.locOption = option
//        mLocationClient!!.start()
//
//
//
////        Toast.makeText(this, myLocationListener.addr, Toast.LENGTH_LONG).show()
////        myLocationListener.onReceiveLocation()
//    }

    fun ddd() {
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

}



