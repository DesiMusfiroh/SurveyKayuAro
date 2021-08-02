package com.ptpn.surveykayuaro.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ptpn.surveykayuaro.R
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
            tvNamaKedai.text = survey.namaKedai
            tvAlamatKedai.text = survey.alamatKedai
            tvNamaNarasumber.text = survey.namaNarasumber
            tvPosisiNarasumber.text = survey.posisiNarasumber
            tvLamaBerjualan.text = survey.lamaBerjualan
            tvTelpKedai.text = survey.telpKedai
            tvSaran.text = survey.saran
            tvBantuan.text = survey.bantuan
            tvJikaTidak.text = survey.jikaTidak
            tvMauJualTehkayuaro.text = survey.mauJualTehkayuaro
            tvKenalTehkayuaro.text = survey.kenalTehkayuaro

            Glide.with(this@DetailActivity)
                .load(survey.image)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.ic_error))
                .into(imgImage)
        }
    }
}