package com.ptpn.surveykayuaro.data.source.remote

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.data.source.remote.response.SurveyResponse

class RemoteDataSource {

    private lateinit var database: DatabaseReference

    companion object {
        fun getInstance(): RemoteDataSource {
            return RemoteDataSource()
        }
        private const val TAG = "RemoteDataSource"
    }

    init {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    fun insertToFirebase(survey: SurveyEntity, imageUri: Uri) {
        val fileName = StringBuilder("${survey.namaNarasumber} - ${survey.addedTime}.jpg")
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")
        storageReference.putFile(imageUri).addOnSuccessListener {uploadImage ->
            storageReference.downloadUrl.addOnSuccessListener { firebaseImageUri ->
                survey.image = firebaseImageUri.toString()
                database = FirebaseDatabase.getInstance().getReference("surveys")
                database.child(survey.id).setValue(survey).addOnSuccessListener {
                    Log.d(TAG, "insert data to firebase success")
                }.addOnFailureListener {
                    Log.d(TAG, "insert data to firebase failed : $it")
                }
            }
            Log.d(TAG, "sukses upload image $uploadImage")
        }.addOnFailureListener {
            Log.d(TAG, "gagal upload image $it")
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
                                survey?.image,
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

    fun updateSurveyOnFirebase(survey: SurveyEntity, imageUri: Uri) {
        val fileName = StringBuilder("${survey.namaNarasumber} - ${survey.addedTime}.jpg")
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")
        storageReference.delete()
        storageReference.putFile(imageUri).addOnSuccessListener { uploadImage ->
            storageReference.downloadUrl.addOnSuccessListener { firebaseImageUri ->
                survey.image = firebaseImageUri.toString()
                database = FirebaseDatabase.getInstance().getReference("surveys")
                database.child(survey.id).setValue(survey).addOnSuccessListener {
                    Log.d(TAG, "update data to firebase success")
                }.addOnFailureListener {
                    Log.d(TAG, "update data to firebase failed : $it")
                }
            }
            Log.d(TAG, "sukses upload image $uploadImage")
        }.addOnFailureListener {
            Log.d(TAG, "gagal upload image $it")
        }
    }

    fun deleteSurveyOnFirebase(surveyId: String, surveyImage: String) {
        database = FirebaseDatabase.getInstance().getReference("surveys")
        database.child(surveyId).removeValue().addOnSuccessListener {
            val storageReference = FirebaseStorage.getInstance().getReference("images/$surveyImage")
            storageReference.delete().addOnSuccessListener {
                Log.d(TAG, "delete image $surveyImage success : $it")
            }.addOnFailureListener {
                Log.d(TAG, "delete image $surveyImage success : $it")
            }
            Log.d(TAG, "delete data $surveyId success : $it")
        }.addOnFailureListener {
            Log.d(TAG, "delete data $surveyId failed failed : $it")
        }
    }
}