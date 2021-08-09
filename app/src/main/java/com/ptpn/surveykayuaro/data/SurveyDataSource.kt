package com.ptpn.surveykayuaro.data

import android.net.Uri
import androidx.lifecycle.LiveData
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.data.source.remote.response.SurveyResponse

interface SurveyDataSource {
    fun getSurveys(): LiveData<List<SurveyEntity>>
    fun getAllSurveys(): LiveData<ArrayList<SurveyResponse>>
    fun getSurvey(surveyId: String) : LiveData<SurveyEntity>
    fun insertSurvey(survey: SurveyEntity, imageUri: Uri)
    suspend fun deleteSurvey(surveyId: String, surveyImage: String)
    suspend fun updateSurvey(survey: SurveyEntity, imageUri: Uri)
}