package com.ptpn.surveykayuaro.ui.form

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.ptpn.surveykayuaro.data.SurveyRepository
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class FormViewModel(private val surveyRepository: SurveyRepository) : ViewModel() {

    fun insert(survey: SurveyEntity, imageUri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            val surveyEntity = SurveyEntity(
                    survey.id,
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
                    survey.addedTime
            )
            surveyRepository.insertSurvey(surveyEntity, imageUri)
        }
    }
}