package com.ptpn.surveykayuaro.data.source.remote

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.ptpn.surveykayuaro.data.source.remote.response.SurveyResponse
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity

class RemoteDataSource {

    private lateinit var database: DatabaseReference

    companion object {
        fun getInstance(): RemoteDataSource {
            return RemoteDataSource()
        }
        private const val TAG = "RemoteDataSource"
    }

    fun insertToFirebase(survey: SurveyEntity, imageUri: Uri) {
        val fileName = StringBuilder("${survey.namaNarasumber} - ${survey.addedTime}.jpg")
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")
        storageReference.putFile(imageUri).addOnSuccessListener {
            Log.d("firebase", "sukses upload image $it")
        }.addOnFailureListener {
            Log.d("firebase", "gagal upload image $it")
        }

        database = FirebaseDatabase.getInstance().getReference("surveys")
        database.child(survey.id).setValue(survey).addOnSuccessListener {
            Log.d(TAG, "insert data to firebase success")
        }.addOnFailureListener {
            Log.d(TAG, "insert data to firebase failed : $it")
        }
    }

    fun getAllSurveysFromFirebase() : LiveData<ArrayList<SurveyResponse>> {
        val surveyResults = MutableLiveData<ArrayList<SurveyResponse>>()
        database = FirebaseDatabase.getInstance().getReference("surveys")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val surveyList = ArrayList<SurveyResponse>()
                if (snapshot.exists()) {
                    for (surveySnapshot in snapshot.children) {
                        val survey = surveySnapshot.getValue(SurveyResponse::class.java)
                        val surveyItem = SurveyResponse(
                                survey?.id,
                                survey?.namaKedai,
                                survey?.alamatKedai,
                                survey?.telpKedai,
                                survey?.namaNarasumber,
                                survey?.posisiNarasumber,
                                survey?.lamaBerjualan,
                                survey?.tehDijual,
                                survey?.kenalTehkayuaro,
                                survey?.tehTerlaris,
                                survey?.hargaTermurah,
                                survey?.mauJualTehkayuaro,
                                survey?.jikaTidak,
                                survey?.bantuan,
                                survey?.namaSurveyor,
                                survey?.saran,
                                "https://firebasestorage.googleapis.com/v0/b/surveykayuaro.appspot.com/o/images/${survey?.image}",
                                survey?.addedTime
                        )
                        surveyList.add(surveyItem)
                    }
                }
                surveyResults.postValue(surveyList)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("firebase", "failed to get survey data ${error.message}")
            }
        })
        return surveyResults
    }
}