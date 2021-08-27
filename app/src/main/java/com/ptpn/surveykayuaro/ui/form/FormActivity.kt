@file:Suppress("DEPRECATION")

package com.ptpn.surveykayuaro.ui.form

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.databinding.ActivityFormBinding
import com.ptpn.surveykayuaro.viewmodel.ViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    private lateinit var viewModel: FormViewModel
    private lateinit var photoPath: String
    private lateinit var addedTime: String
    private lateinit var fileName: String
    private lateinit var imageUri: Uri
    private lateinit var imageString: String
    private var checkedRbKenalTehKayuAro = -1
    private var checkedRbMauJualTeh = -1
    private lateinit var kenalTehKayuAro: String
    private lateinit var mauJualTehKayuAro: String
    private val REQUEST_TAKE_PHOTO = 100
    private val REQUEST_CHOOSE_IMAGE = 200
    private val PERMISSION_CODE = 1001

    companion object {
        const val EXTRA_RESULT = "extra_result_form"
        const val RESULT_CODE_FORM = 110
    }

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
        val datetimeFormat = SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.getDefault())
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
                selectImage()
            }
        } else {
            selectImage()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CHOOSE_IMAGE)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager) != null) {
            val photoFile: File? = try {
                createImageFile()
            } catch (e: IOException) {
                null
            }
            photoFile?.also {
                imageUri  = FileProvider.getUriForFile(this, "com.ptpn.surveykayuaro.fileprovider", it)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    private fun createImageFile(): File? {
        val namaNarasumber = binding.tvNamaNarasumber.text.toString()
        fileName = "$namaNarasumber - $addedTime"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir).apply {
            photoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                binding.picture.visibility = VISIBLE
                binding.warningImage.visibility = GONE
                binding.picture.setImageURI(imageUri)
                imageString = encoder(imageUri)
            }
            if (requestCode == REQUEST_CHOOSE_IMAGE) {
                imageUri = data?.data!!
                val namaNarasumber = binding.tvNamaNarasumber.text.toString()
                fileName = "$namaNarasumber - $addedTime"
                binding.picture.visibility = VISIBLE
                binding.warningImage.visibility = GONE
                binding.picture.setImageURI(imageUri)
                imageString = encoder(imageUri)
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
            val tehTerlaris = tvTehTerlaris.text.toString()
            val hargaTermurah = tvHargaTermurah.text.toString()
            val jikaTidak = tvJikaTidak.text.toString()
            val bantuan = tvBantuan.text.toString()
            val namaSurveyor = tvNamaSurveyor.text.toString()
            val saran = tvSaran.text.toString()
            checkedRbKenalTehKayuAro = rgKenalTehkayuaro.checkedRadioButtonId
            checkedRbMauJualTeh = rgMauJualTehkayuaro.checkedRadioButtonId

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

            if (!::imageUri.isInitialized) {
                warningImage.visibility = VISIBLE
                warningImage.requestFocus()
                return
            }

            if (checkedRbKenalTehKayuAro == -1) {
                rgKenalTehkayuaro.requestFocus()
                return
            } else {
                kenalTehKayuAro = resources.getResourceEntryName(checkedRbKenalTehKayuAro)
            }
            if (checkedRbMauJualTeh == -1) {
                rgKenalTehkayuaro.requestFocus()
                return
            } else {
                mauJualTehKayuAro = resources.getResourceEntryName(checkedRbMauJualTeh)
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
                    kenalTehKayuAro,
                    tehTerlaris,
                    hargaTermurah,
                    mauJualTehKayuAro,
                    jikaTidak,
                    bantuan,
                    namaSurveyor,
                    saran,
                    imageString,
                    addedTime)
            viewModel.insert(survey, imageUri)
        }

        val resultFormIntent = Intent()
        resultFormIntent.putExtra(EXTRA_RESULT, true)
        setResult(RESULT_CODE_FORM, resultFormIntent)
        finish()
    }

    @SuppressLint("NewApi")
    fun encoder(uri: Uri): String{
        val byteArrayOutputStream = ByteArrayOutputStream()
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.getEncoder().encodeToString(imageBytes)
    }
}