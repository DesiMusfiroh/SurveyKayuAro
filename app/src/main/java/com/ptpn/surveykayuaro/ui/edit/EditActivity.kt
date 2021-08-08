package com.ptpn.surveykayuaro.ui.edit

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View.VISIBLE
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.databinding.ActivityEditBinding
import com.ptpn.surveykayuaro.viewmodel.ViewModelFactory
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
@SuppressLint("QueryPermissionsNeeded")
class EditActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_SURVEY_UPDATE = "extra_survey_update_id"
        const val EXTRA_RESULT_UPDATE = "extra_result_form_update"
        const val RESULT_CODE_FORM_UPDATE = 120
    }

    private val REQUEST_TAKE_PHOTO_UPDATE = 101
    private val REQUEST_CHOOSE_IMAGE_UPDATE = 201
    private val PERMISSION_CODE = 1002
    private lateinit var binding: ActivityEditBinding
    private lateinit var viewModel: EditViewModel
    private lateinit var survey: SurveyEntity
    private lateinit var imageUri: Uri
    private lateinit var newFileName: String
    private lateinit var photoPath: String
    private lateinit var updateTime: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Edit Data Survey"

        survey = intent.getParcelableExtra(EXTRA_SURVEY_UPDATE)!!

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[EditViewModel::class.java]

        binding.apply {
            tvNamaKedai.setText(survey.namaKedai)
            tvAlamatKedai.setText(survey.alamatKedai)
            tvNamaNarasumber.setText(survey.namaNarasumber)
            tvPosisiNarasumber.setText(survey.posisiNarasumber)
            tvTelpKedai.setText(survey.telpKedai)
            tvLamaBerjualan.setText(survey.lamaBerjualan)
            tvHargaTermurah.setText(survey.hargaTermurah)
            tvKenalTehkayuaro.setText(survey.kenalTehkayuaro)
            tvMauJualTehkayuaro.setText(survey.mauJualTehkayuaro)
            tvTehTerlaris.setText(survey.tehTerlaris)
            tvJikaTidak.setText(survey.jikaTidak)
            tvBantuan.setText(survey.bantuan)
            tvSaran.setText(survey.saran)
            tvNamaSurveyor.setText(survey.namaSurveyor)
            tvTehDijual.setText(survey.tehDijual)

            picture.visibility = VISIBLE
            picture.setImageURI(Uri.parse(survey.image))
        }
        imageUri = Uri.parse(survey.image)

        val date = Calendar.getInstance().time
        val datetimeFormat = SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.getDefault())
        updateTime = datetimeFormat.format(date)

        binding.btnUpdate.setOnClickListener{ updateDataSurvey() }
        binding.btnTakePicture.setOnClickListener{ takePicture() }
        binding.btnChoosePhoto.setOnClickListener { choosePhoto() }
    }

    private fun choosePhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            } else{
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CHOOSE_IMAGE_UPDATE)
            }
        } else {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CHOOSE_IMAGE_UPDATE)
        }
    }

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
                startActivityForResult(intent, REQUEST_TAKE_PHOTO_UPDATE)
            }
        }
    }

    private fun createImageFile(): File? {
        newFileName = "${survey.namaNarasumber} - ${survey.addedTime}"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(newFileName, ".jpg", storageDir)
        photoPath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO_UPDATE) {
                binding.picture.visibility = VISIBLE
                binding.picture.setImageURI(imageUri)
            }

            if (requestCode == REQUEST_CHOOSE_IMAGE_UPDATE) {
                imageUri = data?.data!!
                binding.picture.visibility = VISIBLE
                binding.picture.setImageURI(imageUri)
            }
        }
    }

    private fun updateDataSurvey() {
        binding.apply {
            val namakedai = tvNamaKedai.text.toString()
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
                survey.id,
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
                survey.addedTime)
            viewModel.updateSurvey(survey, imageUri)
        }

        val resultUpdateIntent = Intent()
        resultUpdateIntent.putExtra(EXTRA_RESULT_UPDATE, true)
        setResult(RESULT_CODE_FORM_UPDATE, resultUpdateIntent)
        finish()
    }
}