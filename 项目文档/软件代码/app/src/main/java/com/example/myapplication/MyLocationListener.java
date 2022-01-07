package com.example.myapplication;

import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

public class MyLocationListener extends BDAbstractLocationListener {
    public String addr ;
    public String country ;
    public String province ;
    public String city ;
    public String district ;
    public String street ;
    public String adcode ;
    public String town ;

    @Override
    public void onReceiveLocation(BDLocation location){
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取地址相关的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

        addr = location.getAddrStr();    //获取详细地址信息
        country = location.getCountry();    //获取国家
        province = location.getProvince();    //获取省份
        city = location.getCity();    //获取城市
        district = location.getDistrict();    //获取区县
        street = location.getStreet();    //获取街道信息
        adcode = location.getAdCode();    //获取adcode
        town = location.getTown();    //获取乡镇信息


    }
}
