package com.ptpn.surveykayuaro.ui.detaillocal

import android.app.Dialog
import android.content.Intent
import android.database.CursorWindow
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.ptpn.surveykayuaro.R
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.databinding.ActivityDetailLocalBinding
import com.ptpn.surveykayuaro.ui.edit.EditActivity
import com.ptpn.surveykayuaro.ui.edit.EditActivity.Companion.EXTRA_RESULT_UPDATE
import com.ptpn.surveykayuaro.ui.edit.EditActivity.Companion.RESULT_CODE_FORM_UPDATE
import com.ptpn.surveykayuaro.viewmodel.ViewModelFactory
import java.lang.StringBuilder
import java.util.*

@Suppress("DEPRECATION")
@RequiresApi(Build.VERSION_CODES.O)
class DetailLocalActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val EXTRA_SURVEY_ID = "extra_survey_id"
        private const val REQUEST_CODE_FORM_UPDATE = 100
    }

    private lateinit var binding: ActivityDetailLocalBinding
    private lateinit var viewModel: DetailLocalViewModel
    private lateinit var surveyId: String
    private lateinit var surveyImage: String
    private lateinit var survey: SurveyEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLocalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Data Survey"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        surveyId = intent.getStringExtra(EXTRA_SURVEY_ID)!!

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailLocalViewModel::class.java]

        viewModel.getSurvey(surveyId).observe(this, {
            if (it != null) {
                survey = it
                surveyImage = "${survey.namaNarasumber} - ${survey.addedTime}.jpg"
                populateSurvey(it)
            }
        })
        binding.btnUpdate.setOnClickListener(this)
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
            tvMauJualTehkayuaro.text = survey.mauJualTehkayuaro
            tvKenalTehkayuaro.text = survey.kenalTehkayuaro
            tvTehDijual.text = survey.tehDijual
            tvTehTerlaris.text = survey.tehTerlaris
            tvHargaTermurah.text = survey.hargaTermurah
            tvNamaSurveyor.text = survey.namaSurveyor
            tvAddedTime.text = survey.addedTime

            val bytes: ByteArray = Base64.getDecoder().decode(survey.image)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            imgImage.setImageBitmap(bitmap)
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
                        viewModel.deleteSurvey(surveyId, surveyImage)
                        finish()
                    }
                    show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_FORM_UPDATE) {
            if (resultCode == RESULT_CODE_FORM_UPDATE) {
                val result = data?.getBooleanExtra(EXTRA_RESULT_UPDATE, false)
                result?.let { showAlert(it) }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_update -> {
                val updateIntent = Intent(this@DetailLocalActivity, EditActivity::class.java)
                updateIntent.putExtra(EditActivity.EXTRA_SURVEY_UPDATE, survey.id)
                startActivityForResult(updateIntent, REQUEST_CODE_FORM_UPDATE)
            }
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
            tvMessage.text = StringBuilder( "Sukses!... \nData Survey \nberhasil di update!")
        } else {
            lottie.setAnimation("failed.json")
            tvMessage.text = StringBuilder("Gagal !!. \nData Survey \nbelum berhasil di update!")
        }
        dialog.show()
    }
}