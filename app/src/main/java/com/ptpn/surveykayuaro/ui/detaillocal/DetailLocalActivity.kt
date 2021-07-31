package com.ptpn.surveykayuaro.ui.detaillocal

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.ptpn.surveykayuaro.R
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.databinding.ActivityDetailLocalBinding
import com.ptpn.surveykayuaro.viewmodel.ViewModelFactory

class DetailLocalActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_SURVEY_ID = "extra_survey_id"
    }
    private lateinit var binding: ActivityDetailLocalBinding
    private lateinit var viewModel: DetailLocalViewModel
    private lateinit var surveyId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLocalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Data Survey"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        surveyId = intent.getStringExtra(EXTRA_SURVEY_ID)!!

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailLocalViewModel::class.java]

        viewModel.getSurvey(surveyId).observe(this, { survey ->
            if (survey != null) {
                populateSurvey(survey)
            }
        })

    }

    private fun populateSurvey(survey: SurveyEntity) {
        binding.apply {
            tvNamaKedai.text = survey.namaKedai
            tvAlamatKedai.text = survey.alamatKedai
            tvNamaNarasumber.text = survey.namaNarasumber
            tvPosisiNarasumber.text = survey.posisiNarasumber
            tvLamaBerjualan.text = survey.lamaBerjualan
            tvTelpKedai.text = survey.telpKedai
            tvSaran.text = survey.saran
            tvBantuan.text = survey.bantuan
            tvJikaTidak.text = survey.jikaTidak

            val imageUri = Uri.parse(survey.image)
            imgImage.setImageURI(imageUri)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_local, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                AlertDialog.Builder(this).apply {
                    setMessage(getString(R.string.delete_alert))
                    setNegativeButton(getString(R.string.no), null)
                    setPositiveButton(getString(R.string.yes)) { _, _ ->
                        viewModel.deleteSurvey(surveyId)
                        finish()
                    }
                    show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}