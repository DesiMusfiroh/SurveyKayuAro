package com.ptpn.surveykayuaro.form

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.ptpn.surveykayuaro.MainActivity
import com.ptpn.surveykayuaro.R
import com.ptpn.surveykayuaro.data.SurveyEntity
import com.ptpn.surveykayuaro.databinding.ActivityFormBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

@Suppress("DEPRECATION")
class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    private lateinit var viewModel: FormViewModel
    private lateinit var photoPath: String
    private val REQUEST_TAKE_PHOTO = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(FormViewModel::class.java)

        if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_TAKE_PHOTO)

        binding.btnSave.setOnClickListener{
            saveDataSurvey()
            Toast.makeText(this, getString(R.string.save_data_success), Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.btnTakePicture.setOnClickListener{
            takePicture()
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {}
            if (photoFile != null) {
                val photoUrl = FileProvider.getUriForFile(
                        this,
                        "com.ptpn.surveykayuaro.fileprovider",
                        photoFile,
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUrl)
                startActivityForResult(intent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            binding.picture.rotation = 90f
            binding.picture.setImageURI(Uri.parse(photoPath))
        }
    }

    private fun createImageFile(): File? {
        val fileName = "NarasumberPicture"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                fileName,
                ".jpg",
                storageDir
        )
        photoPath = image.absolutePath
        return image
    }

    @SuppressLint("SimpleDateFormat")
    private fun saveDataSurvey() {
        val date = Calendar.getInstance().time
        val datetimeFormat = SimpleDateFormat("yyyy/MM/dd hh:mm")
        val addedTime = datetimeFormat.format(date)
        val id = UUID.randomUUID().toString()
        binding.apply {
            val namakedai: String = tvNamaKedai.text.toString()
            val alamatKedai = tvAlamatKedai.text.toString()
            val telpKedai = tvTelpKedai.text.toString()
            val namaNarasumber = tvNamaNarasumber.text.toString()
            val posisiNarasumber = tvPosisiNarasumber.text.toString()
            val lamaBerjualan = tvLamaBerjualan.text.toString()
            val tehDijual = tvTehDijual.text.toString()
            val kenalTehkayuaro = tvKenalTehkayuaro.text.toString()
            val tehTerlaris = tvTehTerlaris.text.toString()
            val hargaTermurah = tvHargaTermurah.text.toString()
            val mauJualTehkayuaro = tvMauJualTehkayuaro.text.toString()
            val jikaTidak = tvJikaTidak.text.toString()
            val bantuan = tvBantuan.text.toString()
            val namaSurveyor = tvNamaSurveyor.text.toString()
            val saran = tvSaran.text.toString()
            val survey = SurveyEntity(
                    id,
                    namakedai,
                    alamatKedai,
                    telpKedai,
                    namaNarasumber,
                    posisiNarasumber,
                    lamaBerjualan,
                    tehDijual,
                    kenalTehkayuaro,
                    tehTerlaris,
                    hargaTermurah,
                    mauJualTehkayuaro,
                    jikaTidak,
                    bantuan,
                    namaSurveyor,
                    saran,
                    addedTime)
            viewModel.insert(survey)
        }
    }


}