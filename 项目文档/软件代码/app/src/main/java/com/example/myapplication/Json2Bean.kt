package com.example.myapplication
import com.contrarywind.interfaces.IPickerViewData

class Json2Bean {
}

//存放省以及所属市
data class PCACodePO(
    val code: String,
    val name: String,
    val children: MutableList<CCodePO>
)

//存放市以及所属辖区
data class CCodePO(
    val code: String,
    val name: String,
    val children: MutableList<AddressInfoPO>
)

//用于显示PickerView显示
data class AddressInfoPO(
    //地区编码
    val code: String,
    //地区名称
    val name: String
) : IPickerViewData {
    override fun getPickerViewText(): String = name
}
