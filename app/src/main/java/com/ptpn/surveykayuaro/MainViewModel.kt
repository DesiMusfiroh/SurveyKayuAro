package com.ptpn.surveykayuaro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ptpn.surveykayuaro.data.source.local.room.SurveyDao
import com.ptpn.surveykayuaro.data.source.local.room.SurveyDatabase
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var surveyDao: SurveyDao?
    private var surveyDb : SurveyDatabase? = SurveyDatabase.getDatabase(application)

    init {
        surveyDao = surveyDb?.surveyDao()
    }

    fun getSurveys(): LiveData<List<SurveyEntity>>? {
        return surveyDao?.getSurveys()
    }
}