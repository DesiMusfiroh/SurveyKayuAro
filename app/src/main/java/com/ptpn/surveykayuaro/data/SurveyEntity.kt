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

    @ColumnInfo(name = "added_time")
    val addedTime: String? = null,
) : Parcelable