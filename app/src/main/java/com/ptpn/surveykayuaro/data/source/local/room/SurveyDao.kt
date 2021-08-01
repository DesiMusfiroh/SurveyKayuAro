package com.ptpn.surveykayuaro.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity

@Dao
interface SurveyDao {
    @Insert
    fun insert(surveyEntity: SurveyEntity)

    @Query("SELECT * FROM data_survey ORDER BY added_time DESC")
    fun getSurveys(): LiveData<List<SurveyEntity>>

    @Query("SELECT * FROM data_survey WHERE id = :id")
    fun getSurvey(id: String): LiveData<SurveyEntity>

    @Query("DELETE FROM data_survey WHERE id = :id")
    suspend fun deleteSurvey(id: String): Int

    @Update
    suspend fun updateSurvey(survey: SurveyEntity)
}