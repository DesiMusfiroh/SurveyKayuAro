package com.ptpn.surveykayuaro.data

import androidx.lifecycle.LiveData
import com.ptpn.surveykayuaro.data.source.local.LocalDataSource
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.data.source.remote.RemoteDataSource
import com.ptpn.surveykayuaro.utils.AppExecutors
import com.ptpn.surveykayuaro.vo.Resource

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

    override fun getSurveys(): LiveData<Resource<List<SurveyEntity>>> {
        TODO("Not yet implemented")
    }

}