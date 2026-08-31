package com.mahvagallery.app.model

data class CustomerInfo(
    val name: String = "",
    val phone: String = "",
    val nationalId: String = "",
    val paymentMethod: String = "کارتخوان", // کارتخوان, نقدی, چک, حواله پایا
    val bankName: String = "",
    val trackingCode: String = "",
    val note: String = ""
) {
    val isNotEmpty: Boolean
        get() = name.isNotBlank() || phone.isNotBlank() || bankName.isNotBlank() || trackingCode.isNotBlank() || note.isNotBlank()
}
