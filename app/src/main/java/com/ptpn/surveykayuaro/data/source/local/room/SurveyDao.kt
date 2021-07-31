package com.ptpn.surveykayuaro.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.vo.Resource

@Dao
interface SurveyDao {
    @Insert
    fun insert(surveyEntity: SurveyEntity)

    @Query("SELECT * FROM data_survey")
    fun getSurveys(): LiveData<List<SurveyEntity>>
}