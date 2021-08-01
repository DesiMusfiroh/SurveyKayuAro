package com.ptpn.surveykayuaro.ui.edit

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.ptpn.surveykayuaro.data.SurveyRepository
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity

class EditViewModel(private val surveyRepository: SurveyRepository) : ViewModel() {

    fun updateSurvey(survey: SurveyEntity, imageUri: Uri) {
        surveyRepository.updateSurvey(survey, imageUri)
    }
}