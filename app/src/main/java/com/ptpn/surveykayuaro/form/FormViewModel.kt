package com.ptpn.surveykayuaro.form

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ptpn.surveykayuaro.data.source.local.room.SurveyDao
import com.ptpn.surveykayuaro.data.source.local.room.SurveyDatabase
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class FormViewModel(application: Application) : AndroidViewModel(application) {

    private var surveyDao: SurveyDao?
    private var surveyDb : SurveyDatabase? = SurveyDatabase.getDatabase(application)

    init {
        surveyDao = surveyDb?.surveyDao()
    }

    fun insert(surveyEntity: SurveyEntity) {
        insertToDb(surveyEntity)
    }

    private fun insertToDb(survey: SurveyEntity) {
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
            surveyDao?.insert(surveyEntity)
        }
    }
}