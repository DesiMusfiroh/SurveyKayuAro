package com.ptpn.surveykayuaro.data.source.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TotalSurveyResponse(
        var totalSurvey: Int? = 0,
        var totalMauJual: Int? = 0,
        var totalTidakMauJual: Int? = 0,
        var totalSudahKenal: Int? = 0,
        var totalBelumKenal: Int? = 0
) : Parcelable