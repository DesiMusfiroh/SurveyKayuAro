package com.ptpn.surveykayuaro.form

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.ptpn.surveykayuaro.data.SurveyEntity
import com.ptpn.surveykayuaro.databinding.ActivityFormBinding
import java.text.SimpleDateFormat
import java.util.*

class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    private lateinit var viewModel: FormViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(FormViewModel::class.java)

        binding.btnSave.setOnClickListener{
            saveDataSurvey()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun saveDataSurvey() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        val date = Calendar.getInstance().time
        val datetimeFormat = SimpleDateFormat("yyyy-MM-dd hh:mm")
        val addedTime = datetimeFormat.format(date)
        val id = UUID.randomUUID().toString()
        binding.apply {
            val namakedai: String = tvNamaKedai.text.toString()
            val alamatKedai = tvAlamatKedai.text.toString()
            val telpKedai = tvTelpKedai.text.toString()
            val survey = SurveyEntity(id, namakedai, alamatKedai, telpKedai, addedTime)
            viewModel.insert(survey)
        }
    }
}