package com.ptpn.surveykayuaro.data

import androidx.lifecycle.LiveData
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.vo.Resource

interface SurveyDataSource {
    fun getSurveys(): LiveData<Resource<List<SurveyEntity>>>
}