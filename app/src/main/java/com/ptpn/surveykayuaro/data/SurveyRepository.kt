package com.ptpn.surveykayuaro.data

import android.net.Uri
import androidx.lifecycle.LiveData
import com.ptpn.surveykayuaro.data.source.local.LocalDataSource
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.data.source.remote.RemoteDataSource
import com.ptpn.surveykayuaro.data.source.remote.response.SurveyResponse
import com.ptpn.surveykayuaro.utils.AppExecutors

class SurveyRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
)
    : SurveyDataSource {

    companion object {
        @Volatile
        private var instance: SurveyRepository? = null

        fun getInstance(remoteData: RemoteDataSource, localData: LocalDataSource, appExecutors: AppExecutors): SurveyRepository =
            instance ?: synchronized(this) {
                instance ?: SurveyRepository(remoteData, localData, appExecutors).apply {
                    instance = this
                }
            }
    }

    override fun insertSurvey(survey: SurveyEntity, imageUri: Uri) {
        localDataSource.insertSurvey(survey)
        remoteDataSource.insertToFirebase(survey, imageUri)
    }

    override fun getSurveys(): LiveData<List<SurveyEntity>> = localDataSource.getSurveys()

    override fun getAllSurveys(): LiveData<ArrayList<SurveyResponse>> {
        return remoteDataSource.getAllSurveysFromFirebase()
    }

    override fun getSurvey(surveyId: String) : LiveData<SurveyEntity> {
        return localDataSource.getSurvey(surveyId)
    }

    override suspend fun deleteSurvey(surveyId: String, surveyImage: String) {
        localDataSource.deleteSurvey(surveyId)
        remoteDataSource.deleteSurveyOnFirebase(surveyId, surveyImage)
    }

    override suspend fun updateSurvey(survey: SurveyEntity, imageUri: Uri) {
        localDataSource.updateSurvey(survey)
        remoteDataSource.updateSurveyOnFirebase(survey, imageUri)
    }
}