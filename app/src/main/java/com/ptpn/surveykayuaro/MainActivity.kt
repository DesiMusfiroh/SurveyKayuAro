package com.ptpn.surveykayuaro

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.databinding.ActivityMainBinding
import com.ptpn.surveykayuaro.form.FormActivity
import com.ptpn.surveykayuaro.utils.generateFile
import com.ptpn.surveykayuaro.utils.goToFileIntent

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var surveyList: List<SurveyEntity>
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.getSurveys()?.observe(this, Observer {surveys ->
            if (surveys != null) {
               surveyList = surveys
            }
        })

        binding.btnForm.setOnClickListener(this)
        binding.btnExport.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_form -> {
                startActivity(Intent(this, FormActivity::class.java))
            }
            R.id.btn_export -> {
                exportDatabaseToCSVFile()
            }
        }
    }

    private fun getCSVFileName() : String = "data_survey.csv"

    private fun exportDatabaseToCSVFile() {
        val csvFile = generateFile(this, getCSVFileName())
        if (csvFile != null) {
            csvWriter().open(csvFile, append = false) {
                writeRow(listOf(
                        "Id",
                        "Nama Kedai",
                        "Alamat Kedai",
                        "Telepon Kedai",
                        "Nama yang diinterview",
                        "Posisi yang diinterview",
                        "Berapa lama sudah berjualan ?",
                        "Teh apa saja yang dijual ?",
                        "Apa sudah kenal Teh Kayu Aro ?",
                        "Teh apa yang paling laris di kedai/toko/grosir ini ?",
                        "Berapa harga termurah teh yang dijual di kedai/toko/grosir ini ?",
                        "Mau menjual Teh Kayu Aro ? Kita akan berikan promo dan sale.",
                        "Jika tidak, kenapa ?",
                        "Apa yang dapa kami bantu agar bapak/ibu dapat menjual Teh kayu Aro ?",
                        "Nama Surveyor",
                        "Masukan dan Saran",
                        "Image",
                        "Added Time"))
                surveyList.forEachIndexed { index, survey ->
                    writeRow(listOf(
                            index + 1,
                            survey.namaKedai,
                            survey.alamatKedai,
                            survey.telpKedai,
                            survey.namaNarasumber,
                            survey.posisiNarasumber,
                            survey.lamaBerjualan,
                            survey.tehDijual,
                            survey.kenalTehkayuaro,
                            survey.tehTerlaris,
                            survey.hargaTermurah,
                            survey.mauJualTehkayuaro,
                            survey.jikaTidak,
                            survey.bantuan,
                            survey.namaSurveyor,
                            survey.saran,
                            survey.image,
                            survey.addedTime))
                }
            }

            Toast.makeText(this, getString(R.string.csv_file_generated_text), Toast.LENGTH_LONG).show()
            val intent = goToFileIntent(this, csvFile)
            startActivity(intent)
        } else {
            Toast.makeText(this, getString(R.string.csv_file_not_generated_text), Toast.LENGTH_LONG).show()
        }
    }
}