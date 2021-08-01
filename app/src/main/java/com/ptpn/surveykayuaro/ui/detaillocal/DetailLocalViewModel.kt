package com.ptpn.surveykayuaro.ui.detaillocal

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ptpn.surveykayuaro.data.SurveyRepository
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailLocalViewModel(private val surveyRepository: SurveyRepository) : ViewModel() {
    fun getSurvey(surveyId: String): LiveData<SurveyEntity> = surveyRepository.getSurvey(surveyId)

    fun deleteSurvey(surveyId: String, surveyImage: String) {
        CoroutineScope(Dispatchers.IO).launch {
            surveyRepository.deleteSurvey(surveyId, surveyImage)
        }
    }

    fun updateSurvey(survey: SurveyEntity, imageUri: Uri) {
        surveyRepository.updateSurvey(survey, imageUri)
    }
}