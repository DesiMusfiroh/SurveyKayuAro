@file:Suppress("DEPRECATION")

package com.ptpn.surveykayuaro.ui.main

import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.database.CursorWindow
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.ptpn.surveykayuaro.R
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.databinding.ActivityMainBinding
import com.ptpn.surveykayuaro.ui.detaillocal.DetailLocalActivity
import com.ptpn.surveykayuaro.ui.detaillocal.DetailLocalActivity.Companion.EXTRA_SURVEY_ID
import com.ptpn.surveykayuaro.ui.form.FormActivity
import com.ptpn.surveykayuaro.ui.list.ListActivity
import com.ptpn.surveykayuaro.utils.generateFile
import com.ptpn.surveykayuaro.utils.goToFileIntent
import com.ptpn.surveykayuaro.viewmodel.ViewModelFactory
import java.lang.reflect.Field
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var surveyList: List<SurveyEntity>
    private lateinit var viewModel: MainViewModel
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

        viewModel.totalSurvey().observe(this, {
            binding.tvTotalSurvey.text = StringBuilder("${it.totalSurvey.toString()} Responden")
            binding.tvMauMenjual.text = it.totalMauJual.toString()
            binding.tvTidakMauMenjual.text = it.totalTidakMauJual.toString()
            binding.tvSudahKenal.text = it.totalSudahKenal.toString()
            binding.tvBelumKenal.text = it.totalBelumKenal.toString()
        })

        viewModel.getSurveys().observe(this, { surveys ->
            if (surveys !== null) {
                surveyList = surveys

                binding.rvSurveys.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                binding.rvSurveys.setHasFixedSize(true)
                val adapter = MainAdapter(surveyList)
                binding.rvSurveys.adapter = adapter
                adapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onItemClicked(data: SurveyEntity) {
                        val detailIntent = Intent(this@MainActivity, DetailLocalActivity::class.java)
                        detailIntent.putExtra(EXTRA_SURVEY_ID, data.id)
                        startActivity(detailIntent)
                    }
                })
            }
        })

        binding.btnForm.setOnClickListener(this)
        binding.btnExport.setOnClickListener(this)
        binding.btnList.setOnClickListener(this)

        // to handle error Row too big to fit into CursorWindow
        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.isAccessible = true
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
                        "No",
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
            tvMessage.text = StringBuilder("Sukses!... \nData Survey \nberhasil ditambahkan!")
        } else {
            lottie.setAnimation("failed.json")
            tvMessage.text = StringBuilder("Gagal !!. \nData Survey \nbelum berhasil disimpan!")
        }
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}