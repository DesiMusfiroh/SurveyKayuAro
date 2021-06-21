package com.ptpn.surveykayuaro.form

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.opencsv.CSVWriter
import com.ptpn.surveykayuaro.data.SurveyDao
import com.ptpn.surveykayuaro.data.SurveyDatabase
import com.ptpn.surveykayuaro.data.SurveyEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter

@Suppress("DEPRECATION")
class FormViewModel(application: Application) : AndroidViewModel(application) {

    private var surveyDao: SurveyDao?
    private var surveyDb : SurveyDatabase? = SurveyDatabase.getDatabase(application)

    init {
        surveyDao = surveyDb?.surveyDao()
    }

    fun insert(surveyEntity: SurveyEntity) {
        insertToDb(surveyEntity)
    }

    private fun insertToDb(survey: SurveyEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val surveyEntity = SurveyEntity(
                survey.id,
                survey.namaKedai,
                survey.alamatKedai,
                survey.telpKedai,
                survey.addedTime
            )
            surveyDao?.insert(surveyEntity)
        }
    }

    fun exportToCSV() {
        val exportDir = File(Environment.getExternalStorageDirectory(), "")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val file = File(exportDir, "survey.csv")
        try {
            file.createNewFile()
            val csvWrite = CSVWriter(FileWriter(file))
            val curCSV: Cursor = surveyDb!!.query("SELECT * FROM data_survey", null)
            csvWrite.writeNext(curCSV.getColumnNames())
            while (curCSV.moveToNext()) {
                //Which column you want to export
                val arrStr = arrayOfNulls<String>(curCSV.getColumnCount())
                for (i in 0 until curCSV.getColumnCount() - 1) arrStr[i] = curCSV.getString(i)
                csvWrite.writeNext(arrStr)
            }
            csvWrite.close()
            curCSV.close()
            Log.d("FormActivity", "Export Success ")
        } catch (sqlEx: Exception) {
            Log.e("FormActivity", sqlEx.message, sqlEx)
        }
    }
}