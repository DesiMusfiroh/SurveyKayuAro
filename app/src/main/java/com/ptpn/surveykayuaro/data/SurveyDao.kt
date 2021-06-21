package com.ptpn.surveykayuaro.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SurveyDao {
    @Insert
    fun insert(surveyEntity: SurveyEntity)

    @Query("SELECT * FROM data_survey")
    fun getSurveys(): LiveData<List<SurveyEntity>>


    @get:Query("SELECT * FROM data_survey")
    val allSurveyData: LiveData<List<SurveyEntity>>
}