package com.ptpn.surveykayuaro.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.data.source.remote.response.SurveyResponse
import com.ptpn.surveykayuaro.databinding.ActivityDetailBinding
import com.ptpn.surveykayuaro.viewmodel.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SURVEY = "extra_survey"
    }
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var survey: SurveyResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Detail Data Survey"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        survey = intent.getParcelableExtra(EXTRA_SURVEY)!!
        populateSurvey()
    }

    private fun populateSurvey() {
        binding.apply {
            tvNamaSurveyor.text = survey.namaSurveyor
        }
    }
}