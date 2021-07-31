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

    fun getSurveys(): LiveData<List<SurveyEntity>> = localDataSource.getSurveys()

    fun getAllSurveys(): LiveData<ArrayList<SurveyResponse>> {
        return remoteDataSource.getAllSurveysFromFirebase()
    }

//    override fun getSurveys(): LiveData<Resource<List<SurveyEntity>>> {
//        return object : NetworkBoundResource<List<SurveyEntity>, List<SurveyResponse>>(appExecutors) {
//            public override fun loadFromDB(): LiveData<List<SurveyEntity>> =
//                    localDataSource.getSurveys()
//
//            override fun shouldFetch(data: List<SurveyEntity>?): Boolean =
//                    data == null || data.isEmpty()
//
//            public override fun createCall(): LiveData<ApiResponse<List<SurveyResponse>>> {
//                throw NotImplementedError("Not yet implemented")
//            }
//
//            public override fun saveCallResult(surveyResponse: List<SurveyResponse>) {
//                val surveyList = ArrayList<SurveyEntity>()
//                for (response in surveyResponse) {
//                    val survey = SurveyEntity(response.id,
//                            response.title,
//                            response.description,
//                            response.date,
//                            false,
//                            response.imagePath)
//                    surveyList.add(survey)
//                }
//                localDataSource.insertSurvey(surveyList)
//            }
//        }.asLiveData()
//    }

}