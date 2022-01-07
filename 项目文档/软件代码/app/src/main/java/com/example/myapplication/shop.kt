package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_shop.*
import kotlinx.android.synthetic.main.bar.*

class shop : AppCompatActivity() {

    private lateinit var useraccount:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        supportActionBar?.hide()

        shop_.setImageResource(R.drawable.shopn)
        shoptext.setTextColor(Color.parseColor("#68B2A0"))

        useraccount = intent.getStringExtra("account").toString()
        var class1 = goods(199.9,"运动手环","30种运动模式，24h心率检测，运动数据轻松记录，做您运动的忠实伙伴",
        R.drawable.cuff,R.drawable.deatil1,R.drawable.deatil2)
        var class2 = goods(49.9,"体重称","高精准智能体重秤使用精准传感器，手机连接开启智能生活",
            R.drawable.weight_scale,R.drawable.weight_scale_detail1,R.drawable.weight_scale_detail2)
        var class3 = goods(4999.9,"跑步机","采用AI柔性减震科技，配备豪华跑台、无刷电机、减震气囊，跑感舒适更护膝",
            R.drawable.treadmill,R.drawable.treadmill_detail1,R.drawable.treadmill_detail2)
        var class4 = goods(49.9,"阻力带","采用天然乳胶，亲肤无味韧性强，健身效果更明显",
            R.drawable.res_hand,R.drawable.res_hand_detail1,R.drawable.res_hand_detail2)
        var class5 = goods(59.9,"护腕","采用透气吸汗材质，防止肌肉软通、关节扭伤",
            R.drawable.wrist_guard,R.drawable.wrist_guard_detail1,R.drawable.wrist_guard_detail2)
        var class6 = goods(59.9,"护膝","触感舒适，优良工艺，关节处使用加厚海绵",
            R.drawable.kneepad,R.drawable.kneepad_detail1,R.drawable.kneepad_detail2)
        var class7 = goods(169.9,"运动短袖女","面料柔软，触感亲肤舒适不易变形",
            R.drawable.wshort_sl,R.drawable.wshort_sl_detail1,R.drawable.wshort_sl_detail2)
        var class8 = goods(199.9,"运动外套女","透气速干，轻薄无负担，防泼水面料，抵御小雨",
            R.drawable.wsport_coat,R.drawable.wsport_coat_detail1,R.drawable.wsport_coat_detail2)
        var class9 = goods(199.9,"瑜伽外套","舒适塑形，运动无束缚",
            R.drawable.wyoga_coat,R.drawable.wyoga_coat_detail1,R.drawable.wyoga_coat_detail2)
        var class10 = goods(189.9,"运动短袖","轻薄舒适，耐水洗不褪色",
            R.drawable.mshort_sl,R.drawable.mcoat_detail1,R.drawable.mcoat_detail2)
        var class11 = goods(249.9,"运动外套","甄选速干面料，拒绝粘腻闷热，顺滑亲肤",
            R.drawable.mcoat,R.drawable.mcoat_detail1,R.drawable.mcoat_detail2)
        var class12 = goods(159.9,"运动长裤","宽松坠感，弹力面料舒展不拘束",
            R.drawable.mtrou,R.drawable.mtrou_detail1,R.drawable.mtrou_detail2)
//        val intent = Intent(this,comment3::class.java)
//        val bundle = Bundle()
//        bundle.putSerializable("commentnow", commentnow)
//
//        intent.putExtras(bundle)
//        intent.putExtra("account",useraccount)
//        startActivity(intent)

//        val commentnow :Comment= intent.getSerializableExtra("commentnow") as Comment

        mine.setOnClickListener {
            val intent = Intent(this,com.example.myapplication.mine::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
            mine
        }

        home.setOnClickListener {
            val intent = Intent(this,com.example.myapplication.home::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }

        add.setOnClickListener {
            val intent = Intent(this, new_team::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }

        talk.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }
        shop_to_cart.setOnClickListener {
            val intent = Intent(this,com.example.myapplication.mycart::class.java)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }
        shop_cuff.setOnClickListener {
            val intent = Intent(this, goods_detail::class.java)
            val bundle = Bundle()
            bundle.putSerializable("goods_now", class1)
            intent.putExtras(bundle)
            intent.putExtra("account",useraccount)
            startActivity(intent)

        }
        weight_scales.setOnClickListener {
            val intent = Intent(this, goods_detail::class.java)
            val bundle = Bundle()
            bundle.putSerializable("goods_now", class2)
            intent.putExtras(bundle)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }
        treadmill.setOnClickListener {
            val intent = Intent(this, goods_detail::class.java)
            val bundle = Bundle()
            bundle.putSerializable("goods_now", class3)
            intent.putExtras(bundle)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }
        res_hand.setOnClickListener {
            val intent = Intent(this, goods_detail::class.java)
            val bundle = Bundle()
            bundle.putSerializable("goods_now", class4)
            intent.putExtras(bundle)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }
        wrist_guard.setOnClickListener {
            val intent = Intent(this, goods_detail::class.java)
            val bundle = Bundle()
            bundle.putSerializable("goods_now", class5)
            intent.putExtras(bundle)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }
        kneepad.setOnClickListener {
            val intent = Intent(this, goods_detail::class.java)
            val bundle = Bundle()
            bundle.putSerializable("goods_now", class6)
            intent.putExtras(bundle)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }
        wshort_sl.setOnClickListener {
            val intent = Intent(this, goods_detail::class.java)
            val bundle = Bundle()
            bundle.putSerializable("goods_now", class7)
            intent.putExtras(bundle)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }
        wsport_coat.setOnClickListener {
            val intent = Intent(this, goods_detail::class.java)
            val bundle = Bundle()
            bundle.putSerializable("goods_now", class8)
            intent.putExtras(bundle)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }
        wyoga_coat.setOnClickListener {
            val intent = Intent(this, goods_detail::class.java)
            val bundle = Bundle()
            bundle.putSerializable("goods_now", class9)
            intent.putExtras(bundle)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }
        mshort_sl.setOnClickListener {
            val intent = Intent(this, goods_detail::class.java)
            val bundle = Bundle()
            bundle.putSerializable("goods_now", class10)
            intent.putExtras(bundle)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }
        mcoat.setOnClickListener {
            val intent = Intent(this, goods_detail::class.java)
            val bundle = Bundle()
            bundle.putSerializable("goods_now", class11)
            intent.putExtras(bundle)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }
        mtrou.setOnClickListener {
            val intent = Intent(this, goods_detail::class.java)
            val bundle = Bundle()
            bundle.putSerializable("goods_now", class12)
            intent.putExtras(bundle)
            intent.putExtra("account",useraccount)
            startActivity(intent)
        }

    }
}