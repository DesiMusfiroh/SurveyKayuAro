package com.ptpn.surveykayuaro.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ptpn.surveykayuaro.data.SurveyRepository
import com.ptpn.surveykayuaro.data.source.remote.response.SurveyResponse

class ListViewModel(private val surveyRepository: SurveyRepository) : ViewModel() {
    fun getAllSurveys(): LiveData<ArrayList<SurveyResponse>> = surveyRepository.getAllSurveys()
}