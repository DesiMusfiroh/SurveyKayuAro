package com.ptpn.surveykayuaro.data.source.remote

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.ptpn.surveykayuaro.data.source.remote.response.SurveyResponse

class RemoteDataSource {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var database: DatabaseReference

    companion object {
        fun getInstance(): RemoteDataSource {
            return RemoteDataSource()
        }
        private const val TAG = "RemoteDataSource"
    }

    fun getSurveysData() : LiveData<ArrayList<SurveyResponse>> {
        val surveyResults = MutableLiveData<ArrayList<SurveyResponse>>()
        database = FirebaseDatabase.getInstance().getReference("surveys")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val surveyList = ArrayList<SurveyResponse>()
                if (snapshot.exists()){
                    for (surveySnapshot in snapshot.children) {
                        val survey = surveySnapshot.getValue(SurveyResponse::class.java)
                        val surveyItem = survey?.id?.let {
                            SurveyResponse(
                                it,
                                survey.namaKedai,
                                survey.alamatKedai,
                                survey.telpKedai,
                                survey.namaNarasumber,
                                survey.posisiNarasumber,
                                survey.lamaBerjualan,
                                survey.tehDijual,
                                survey.kenalTehkayuaro,
                                survey.tehTerlaris,
                                survey.hargaTermurah,
                                survey.mauJualTehkayuaro,
                                survey.jikaTidak,
                                survey.bantuan,
                                survey.namaSurveyor,
                                survey.saran,
                                survey.image,
                                survey.addedTime,
                            )
                        }
                        if (surveyItem != null) {
                            surveyList.add(surveyItem)
                        }
                    }
                }
                surveyResults.postValue(surveyList)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("firebase", "gagal")
            }
        })
        return surveyResults
    }
}