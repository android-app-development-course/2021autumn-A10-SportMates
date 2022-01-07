package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_new_team.*

/*
新建队伍new_team.kt

获取login的账号，该用户为队长
获取输入的信息，保存到数据库
*/

class new_team : AppCompatActivity() {
    private lateinit var useraccount:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_team)
        supportActionBar?.hide()

        useraccount = intent.getStringExtra("account").toString()

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

        edit_place1.setOnClickListener{
            showAddressPicker(provinceItems,cityItems,areaItems)
        }

        okk.setOnClickListener {
            // 1
            if (edit_name.text.toString() == "") {
                Toast.makeText(this@new_team, "请输入队伍名称", Toast.LENGTH_LONG).show()
            } else {
                // 2
                if (edit_type.text.toString() == "") {
                    Toast.makeText(this@new_team, "请输入队伍类型", Toast.LENGTH_LONG).show()
                } else {
                    // 3
                    if (edit_num.text.toString() == "") {
                        Toast.makeText(this@new_team, "请输入队伍人数", Toast.LENGTH_LONG).show()
                    } else {
                        // 4
                        if (edit_time.text.toString() == "") {
                            Toast.makeText(this@new_team, "请输入运动时间", Toast.LENGTH_LONG).show()
                        } else {
                            // 5
                            if (edit_place2.text.toString() == "") {
                                Toast.makeText(this@new_team, "请输入运动地点", Toast.LENGTH_LONG).show()
                            } else {
                                // 6
                                /*
                                // 运动描述可以为空
                                if (edit_description.text.toString() == "") {
                                    Toast.makeText(this@new_team, "请输入运动描述", Toast.LENGTH_LONG).show()
                                } else {
                                    // 7
                                }
                                */
                                val a_new_team = team_table()
                                a_new_team.name = edit_name.text.toString()
                                a_new_team.type = edit_type.text.toString()
                                a_new_team.leader = useraccount // 当前登录账号
                                a_new_team.member = ""
                                a_new_team.number = edit_num.text.toString().toInt()
                                a_new_team.time = edit_time.text.toString()
                                a_new_team.place = edit_place2.text.toString()
                                a_new_team.description = edit_description.text.toString()

                                var id = ""
                                a_new_team.save(object : SaveListener<String>() {
                                    override fun done(objectId: String?, ex: BmobException?) {
                                        if (ex == null) {
                                            Toast.makeText(this@new_team, "新建队伍成功", Toast.LENGTH_LONG)
                                                .show()
                                            id = objectId.toString()
                                            finish()
                                            /**
                                             * 跳转到新建的队伍详情页
                                             */
                                            val intent = Intent(this@new_team, leader_view::class.java)
                                            intent.putExtra("team_object_id", id)
                                            setResult(RESULT_OK, intent)
                                            startActivity(intent)
                                        } else {
                                            Log.e("CREATE", "新增数据失败：" + ex.message)
                                        }
                                    }
                                })

                                /**
                                 * 还需要将组队信息更新到User表
                                 */
                            }
                        }
                    }
                }
            }
        }

        no_okk.setOnClickListener {
            finish()
        }
    }

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

                edit_place2.setText(tx)
//                initTeam()

            })
                // .setTitleText(pickerEnum.title)
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build<AddressInfoPO>()
        addressPv.setPicker(provinceItems, cityItems, areaItems)
        addressPv.show()
    }
}