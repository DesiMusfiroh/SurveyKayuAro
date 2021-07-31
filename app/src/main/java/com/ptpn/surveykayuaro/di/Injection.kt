package com.ptpn.surveykayuaro.di

import android.content.Context
import com.ptpn.surveykayuaro.data.SurveyRepository
import com.ptpn.surveykayuaro.data.source.local.LocalDataSource
import com.ptpn.surveykayuaro.data.source.local.room.SurveyDatabase
import com.ptpn.surveykayuaro.data.source.remote.RemoteDataSource
import com.ptpn.surveykayuaro.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): SurveyRepository {
        val database = SurveyDatabase.getInstance(context)
        val remoteDataSource = RemoteDataSource.getInstance()
        val localDataSource = LocalDataSource.getInstance(database.surveyDao())
        val appExecutors = AppExecutors()
        return SurveyRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }
}