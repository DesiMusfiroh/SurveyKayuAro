@file:Suppress("DEPRECATION")

package com.ptpn.surveykayuaro.ui.main

import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.ptpn.surveykayuaro.R
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.databinding.ActivityMainBinding
import com.ptpn.surveykayuaro.ui.form.FormActivity
import com.ptpn.surveykayuaro.ui.list.ListActivity
import com.ptpn.surveykayuaro.utils.generateFile
import com.ptpn.surveykayuaro.utils.goToFileIntent
import com.ptpn.surveykayuaro.viewmodel.ViewModelFactory
import java.lang.StringBuilder

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var surveyList: List<SurveyEntity>
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter
    companion object {
        private const val REQUEST_CODE_FORM = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        viewModel.getSurveys().observe(this, {surveys ->
            if (surveys != null) {
                surveyList = surveys

                binding.rvSurveys.layoutManager = LinearLayoutManager(this)
                adapter = MainAdapter(surveyList)
                binding.rvSurveys.adapter = adapter
                adapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: SurveyEntity) {}
                })
            }
        })

        binding.btnForm.setOnClickListener(this)
        binding.btnExport.setOnClickListener(this)
        binding.btnList.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_form -> {
                val formIntent = Intent(this@MainActivity, FormActivity::class.java)
                startActivityForResult(formIntent, REQUEST_CODE_FORM)
            }
            R.id.btn_export -> {
                exportDatabaseToCSVFile()
            }
            R.id.btn_list -> {
                startActivity(Intent(this, ListActivity::class.java))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_FORM) {
            if (resultCode == FormActivity.RESULT_CODE_FORM) {
                val result = data?.getBooleanExtra(FormActivity.EXTRA_RESULT, false)
                result?.let { showAlert(it) }
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

    private fun showAlert(check: Boolean) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_alert)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val tvMessage = dialog.findViewById(R.id.tv_message) as TextView

        val btnClose = dialog.findViewById(R.id.iv_close) as ImageView
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        val lottie = dialog.findViewById(R.id.lottie_dialog) as LottieAnimationView
        if (check) {
            lottie.setAnimation("success.json")
            tvMessage.text = StringBuilder( "Sukses!... \nData Survey \nberhasil ditambahkan!")
        } else {
            lottie.setAnimation("failed.json")
            tvMessage.text = StringBuilder("Gagal !!. \nData Survey \nbelum berhasil disimpan!")
        }
        dialog.show()
    }
}