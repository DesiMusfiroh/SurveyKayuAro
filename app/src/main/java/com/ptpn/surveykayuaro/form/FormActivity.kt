package com.ptpn.surveykayuaro.form

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ptpn.surveykayuaro.MainActivity
import com.ptpn.surveykayuaro.R
import com.ptpn.surveykayuaro.data.SurveyEntity
import com.ptpn.surveykayuaro.databinding.ActivityFormBinding
import java.text.SimpleDateFormat
import java.util.*

class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    private lateinit var viewModel: FormViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(FormViewModel::class.java)

        binding.btnSave.setOnClickListener{
            saveDataSurvey()
            Toast.makeText(this, getString(R.string.save_data_success), Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun saveDataSurvey() {
        val date = Calendar.getInstance().time
        val datetimeFormat = SimpleDateFormat("yyyy/MM/dd hh:mm")
        val addedTime = datetimeFormat.format(date)
        val id = UUID.randomUUID().toString()
        binding.apply {
            val namakedai: String = tvNamaKedai.text.toString()
            val alamatKedai = tvAlamatKedai.text.toString()
            val telpKedai = tvTelpKedai.text.toString()
            val namaNarasumber = tvNamaNarasumber.text.toString()
            val posisiNarasumber = tvPosisiNarasumber.text.toString()
            val lamaBerjualan = tvLamaBerjualan.text.toString()
            val tehDijual = tvTehDijual.text.toString()
            val kenalTehkayuaro = tvKenalTehkayuaro.text.toString()
            val tehTerlaris = tvTehTerlaris.text.toString()
            val hargaTermurah = tvHargaTermurah.text.toString()
            val mauJualTehkayuaro = tvMauJualTehkayuaro.text.toString()
            val jikaTidak = tvJikaTidak.text.toString()
            val bantuan = tvBantuan.text.toString()
            val namaSurveyor = tvNamaSurveyor.text.toString()
            val saran = tvSaran.text.toString()
            val survey = SurveyEntity(
                    id,
                    namakedai,
                    alamatKedai,
                    telpKedai,
                    namaNarasumber,
                    posisiNarasumber,
                    lamaBerjualan,
                    tehDijual,
                    kenalTehkayuaro,
                    tehTerlaris,
                    hargaTermurah,
                    mauJualTehkayuaro,
                    jikaTidak,
                    bantuan,
                    namaSurveyor,
                    saran,
                    addedTime)
            viewModel.insert(survey)
        }
    }
}