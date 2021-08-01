package com.ptpn.surveykayuaro.ui.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.ptpn.surveykayuaro.R
import com.ptpn.surveykayuaro.data.source.remote.response.SurveyResponse
import com.ptpn.surveykayuaro.databinding.ActivityListBinding
import com.ptpn.surveykayuaro.ui.detail.DetailActivity
import com.ptpn.surveykayuaro.utils.DatePickerFragment
import com.ptpn.surveykayuaro.utils.generateFile
import com.ptpn.surveykayuaro.utils.goToFileIntent
import com.ptpn.surveykayuaro.viewmodel.ViewModelFactory
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class ListActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener, View.OnClickListener{
    private lateinit var binding: ActivityListBinding
    private lateinit var viewModel: ListViewModel
    private lateinit var surveyAdapter: ListAdapter
    private lateinit var surveyResponse: List<SurveyResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "List Data Survey"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ListViewModel::class.java]

        getAllSurveyData()
        searchSurveys()
        binding.btnExport.setOnClickListener(this)
    }

    private fun searchSurveys() {
        binding.searchList.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                surveyAdapter.filter.filter(newText)
                binding.listNote.text = StringBuilder("Hasil Pencarian Data Survey")
                binding.listDesc.text = StringBuilder("Silahkan klik tombol \"Export to CSV\" untuk mengexport data survey hasil pencarian ke dalam format excel.")
                if (newText.isNullOrBlank()) {
                    binding.listNote.text = StringBuilder("Data Survey Keseluruhan")
                    binding.listDesc.text = StringBuilder("Silahkan klik tombol \"Export to CSV\" untuk mengexport keseuruhan data survey ke dalam format excel.")
                }
                return false
            }
        })
    }

    private fun getAllSurveyData() {
        viewModel.getAllSurveys().observe(this, {
            surveyResponse = it
            surveyAdapter = ListAdapter(it, this)
            surveyAdapter.notifyDataSetChanged()

            binding.apply {
                shimmerRvSurveys.stopShimmer()
                shimmerRvSurveys.visibility = GONE
                rvSurveys.layoutManager = LinearLayoutManager(this@ListActivity)
                rvSurveys.setHasFixedSize(true)
                rvSurveys.adapter = surveyAdapter
            }

            surveyAdapter.setOnItemClickCallback(object : ListAdapter.OnItemClickCallback {
                override fun onItemClicked(data: SurveyResponse) {
                    val intent = Intent(this@ListActivity, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_SURVEY, data)
                    startActivity(intent)
                }
            })
        })
    }

    fun showDatePicker(view: View) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val dateChosen = dateFormat.format(calendar.time)
        binding.listNote.text = StringBuilder("Data Survey Tanggal $dateChosen")
        binding.listDesc.text = StringBuilder("Silahkan klik tombol \"Export to CSV\" untuk mengexport data survey tanggal $dateChosen ke dalam format excel.")
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
                surveyResponse.forEachIndexed { index, survey ->
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

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_export -> {
                exportDatabaseToCSVFile()
            }
        }
    }

}