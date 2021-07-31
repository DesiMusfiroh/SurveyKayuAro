package com.ptpn.surveykayuaro.data.source.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SurveyResponse(
    val id: String? = null,
    val namaKedai: String? = null,
    val alamatKedai: String? = null,
    val telpKedai: String? = null,
    val namaNarasumber: String? = null,
    val posisiNarasumber: String? = null,
    val lamaBerjualan: String? = null,
    val tehDijual: String? = null,
    val kenalTehkayuaro: String? = null,
    val tehTerlaris: String? = null,
    val hargaTermurah: String? = null,
    val mauJualTehkayuaro: String? = null,
    val jikaTidak: String? = null,
    val bantuan: String? = null,
    val namaSurveyor: String? = null,
    val saran: String? = null,
    val image: String? = null,
    val addedTime: String? = null,
) : Parcelable {}