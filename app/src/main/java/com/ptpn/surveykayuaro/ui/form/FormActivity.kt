package com.ptpn.surveykayuaro.ui.form

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.storage.FirebaseStorage
import com.ptpn.surveykayuaro.ui.main.MainActivity
import com.ptpn.surveykayuaro.R
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.databinding.ActivityFormBinding
import com.ptpn.surveykayuaro.viewmodel.ViewModelFactory
import java.io.File
import java.io.IOException
import java.lang.StringBuilder
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
@SuppressLint("QueryPermissionsNeeded", "SimpleDateFormat")
class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    private lateinit var viewModel: FormViewModel
    private lateinit var photoPath: String
    private lateinit var addedTime: String
    private lateinit var fileName: String
    private lateinit var imageUri: Uri

    companion object {
        private const val REQUEST_TAKE_PHOTO = 100
        private const val REQUEST_CHOOSE_IMAGE = 200;
        private const val PERMISSION_CODE = 1001;
        const val EXTRA_RESULT = "extra_result_form"
        const val RESULT_CODE_FORM = 110
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Form Survey"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[FormViewModel::class.java]

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_TAKE_PHOTO)

        val date = Calendar.getInstance().time
        val datetimeFormat = SimpleDateFormat("dd-MM-yyyy hh:mm")
        addedTime = datetimeFormat.format(date)

        binding.btnSave.setOnClickListener{ saveDataSurvey() }
        binding.btnTakePicture.setOnClickListener{ takePicture() }
        binding.btnChoosePhoto.setOnClickListener { choosePhoto() }
        fileName = " "
    }

    private fun choosePhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            } else{
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CHOOSE_IMAGE)
            }
        } else {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CHOOSE_IMAGE)
        }
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {}
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(
                        this,
                        "com.ptpn.surveykayuaro.fileprovider",
                        photoFile,
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    private fun createImageFile(): File? {
        val namaNarasumber = binding.tvNamaNarasumber.text.toString()
        fileName = "$namaNarasumber - $addedTime"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                fileName,
                ".jpg",
                storageDir
        )
        photoPath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                binding.picture.visibility = VISIBLE
                binding.picture.setImageURI(imageUri)
            }
            if (requestCode == REQUEST_CHOOSE_IMAGE) {
                imageUri = data?.data!!
                val namaNarasumber = binding.tvNamaNarasumber.text.toString()
                fileName = "$namaNarasumber - $addedTime"
                binding.picture.visibility = VISIBLE
                binding.picture.setImageURI(imageUri)
            }
        }
    }

    private fun saveDataSurvey() {
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

            if (namakedai.isEmpty()) {
                tvNamaKedai.error = "Mohon diisi terlebih dahulu!"
                tvNamaKedai.requestFocus()
                return
            }
            if (alamatKedai.isEmpty()) {
                tvAlamatKedai.error = "Mohon diisi terlebih dahulu!"
                tvAlamatKedai.requestFocus()
                return
            }
            if (namaNarasumber.isEmpty()) {
                tvNamaNarasumber.error = "Mohon diisi terlebih dahulu!"
                tvNamaNarasumber.requestFocus()
                return
            }
            if (posisiNarasumber.isEmpty()) {
                tvPosisiNarasumber.error = "Mohon diisi terlebih dahulu!"
                tvPosisiNarasumber.requestFocus()
                return
            }
            if (kenalTehkayuaro.isEmpty()) {
                tvKenalTehkayuaro.error = "Mohon diisi terlebih dahulu!"
                tvKenalTehkayuaro.requestFocus()
                return
            }
            if (mauJualTehkayuaro.isEmpty()) {
                tvMauJualTehkayuaro.error = "Mohon diisi terlebih dahulu!"
                tvMauJualTehkayuaro.requestFocus()
                return
            }
            if (namaSurveyor.isEmpty()) {
                tvNamaSurveyor.error = "Mohon diisi terlebih dahulu!"
                tvNamaSurveyor.requestFocus()
                return
            }
            if (saran.isEmpty()) {
                tvSaran.error = "Mohon diisi terlebih dahulu!"
                tvSaran.requestFocus()
                return
            }

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
                    imageUri.toString(),
                    addedTime)
            viewModel.insert(survey, imageUri)
        }

        val resultFormIntent = Intent()
        resultFormIntent.putExtra(EXTRA_RESULT, true)
        setResult(RESULT_CODE_FORM, resultFormIntent)
        finish()
    }
}