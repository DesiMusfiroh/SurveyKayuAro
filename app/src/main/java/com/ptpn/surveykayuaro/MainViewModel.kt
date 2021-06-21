package com.ptpn.surveykayuaro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ptpn.surveykayuaro.data.SurveyDao
import com.ptpn.surveykayuaro.data.SurveyDatabase
import com.ptpn.surveykayuaro.data.SurveyEntity

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var surveyDao: SurveyDao?
    private var surveyDb : SurveyDatabase? = SurveyDatabase.getDatabase(application)

    init {
        surveyDao = surveyDb?.surveyDao()
    }
    
    val getAllSurveyData = surveyDao?.allSurveyData

    fun getSurveys(): LiveData<List<SurveyEntity>>? {
        return surveyDao?.getSurveys()
    }
}