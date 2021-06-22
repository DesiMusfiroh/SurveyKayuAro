package com.ptpn.surveykayuaro.data

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "data_survey")
data class SurveyEntity (
    @PrimaryKey
    @NonNull
    val id: String,

    @ColumnInfo(name = "nama_kedai")
    val namaKedai: String? = null,

    @ColumnInfo(name = "alamat_kedai")
    val alamatKedai: String? = null,

    @ColumnInfo(name = "telp_kedai")
    val telpKedai: String? = null,

    @ColumnInfo(name = "nama_narasumber")
    val namaNarasumber: String? = null,

    @ColumnInfo(name = "posisi_narasumber")
    val posisiNarasumber: String? = null,

    @ColumnInfo(name = "lama_berjualan")
    val lamaBerjualan: String? = null,

    @ColumnInfo(name = "teh_dijual")
    val tehDijual: String? = null,

    @ColumnInfo(name = "kenal_tehkayuaro")
    val kenalTehkayuaro: String? = null,

    @ColumnInfo(name = "teh_terlaris")
    val tehTerlaris: String? = null,

    @ColumnInfo(name = "harga_termurah")
    val hargaTermurah: String? = null,

    @ColumnInfo(name = "mau_jual_tehkayuaro")
    val mauJualTehkayuaro: String? = null,

    @ColumnInfo(name = "jika_tidak")
    val jikaTidak: String? = null,

    @ColumnInfo(name = "bantuan")
    val bantuan: String? = null,

    @ColumnInfo(name = "nama_surveyor")
    val namaSurveyor: String? = null,

    @ColumnInfo(name = "saran")
    val saran: String? = null,

    @ColumnInfo(name = "added_time")
    val addedTime: String? = null,
) : Parcelable