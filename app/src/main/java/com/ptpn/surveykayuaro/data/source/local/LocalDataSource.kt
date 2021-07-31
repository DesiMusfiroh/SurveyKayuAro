package com.ptpn.surveykayuaro.data.source.local

import androidx.lifecycle.LiveData
import com.ptpn.surveykayuaro.data.source.local.room.SurveyDao
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.vo.Resource

class LocalDataSource private constructor(private val mSurveyDao: SurveyDao) {

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(surveyDao: SurveyDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(surveyDao)
    }

    fun getSurveys(): LiveData<List<SurveyEntity>> = mSurveyDao.getSurveys()
    fun insertSurvey(survey: SurveyEntity) = mSurveyDao.insert(survey)
    fun getSurvey(surveyId: String): LiveData<SurveyEntity> = mSurveyDao.getSurvey(surveyId)
    suspend fun deleteSurvey(surveyId: String) {
        mSurveyDao.deleteSurvey(surveyId)
    }
}