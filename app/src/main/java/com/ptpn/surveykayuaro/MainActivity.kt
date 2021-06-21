package com.ptpn.surveykayuaro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.ptpn.surveykayuaro.data.SurveyEntity
import com.ptpn.surveykayuaro.databinding.ActivityMainBinding
import com.ptpn.surveykayuaro.form.FormActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var surveyList: List<SurveyEntity>
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                writeRow(listOf("Id", "Nama Kedai", "Alamat Kedai", "Telepon Kedai", "Added Time"))
                surveyList.forEachIndexed { index, survey ->
                    writeRow(listOf(index, survey.namaKedai, survey.alamatKedai, survey.telpKedai, survey.addedTime))
                    Log.d("Survey", "survey data === ${survey.alamatKedai}")
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