package com.ptpn.surveykayuaro.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ptpn.surveykayuaro.data.SurveyRepository
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.vo.Resource

class MainViewModel(private val surveyRepository: SurveyRepository) : ViewModel(){

    fun getSurveys(): LiveData<List<SurveyEntity>> {
        return surveyRepository.getSurveys()
    }
}