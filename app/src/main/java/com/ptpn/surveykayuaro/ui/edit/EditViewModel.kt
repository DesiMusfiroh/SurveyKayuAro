package com.ptpn.surveykayuaro.ui.edit

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.ptpn.surveykayuaro.data.SurveyRepository
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditViewModel(private val surveyRepository: SurveyRepository) : ViewModel() {

    fun updateSurvey(survey: SurveyEntity, imageUri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            surveyRepository.updateSurvey(survey, imageUri)
        }
    }
}