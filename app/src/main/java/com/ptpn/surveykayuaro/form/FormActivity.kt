package com.ptpn.surveykayuaro.form

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.ptpn.surveykayuaro.MainActivity
import com.ptpn.surveykayuaro.R
import com.ptpn.surveykayuaro.data.SurveyEntity
import com.ptpn.surveykayuaro.databinding.ActivityFormBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    private lateinit var viewModel: FormViewModel
    private lateinit var photoPath: String
    private val REQUEST_TAKE_PHOTO = 1
    private lateinit var addedTime: String
    private lateinit var fileName: String

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(FormViewModel::class.java)

        supportActionBar?.title = "Form Survey"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_TAKE_PHOTO)

        val date = Calendar.getInstance().time
        val datetimeFormat = SimpleDateFormat("yyyy-MM-dd hh:mm")
        addedTime = datetimeFormat.format(date)

        binding.btnSave.setOnClickListener{
            saveDataSurvey()
        }

        binding.btnTakePicture.setOnClickListener{
            takePicture()
        }
        fileName = " "
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
            binding.picture.visibility = VISIBLE
            binding.picture.rotation = 90f
            binding.picture.setImageURI(Uri.parse(photoPath))
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

    @SuppressLint("SimpleDateFormat")
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
            if (telpKedai.isEmpty()) {
                tvTelpKedai.error = "Mohon diisi terlebih dahulu!"
                tvTelpKedai.requestFocus()
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
            if (lamaBerjualan.isEmpty()) {
                tvLamaBerjualan.error = "Mohon diisi terlebih dahulu!"
                tvLamaBerjualan.requestFocus()
                return
            }
            if (tehDijual.isEmpty()) {
                tvTehDijual.error = "Mohon diisi terlebih dahulu!"
                tvTehDijual.requestFocus()
                return
            }
            if (kenalTehkayuaro.isEmpty()) {
                tvKenalTehkayuaro.error = "Mohon diisi terlebih dahulu!"
                tvKenalTehkayuaro.requestFocus()
                return
            }
            if (tehTerlaris.isEmpty()) {
                tvTehTerlaris.error = "Mohon diisi terlebih dahulu!"
                tvTehTerlaris.requestFocus()
                return
            }
            if (hargaTermurah.isEmpty()) {
                tvHargaTermurah.error = "Mohon diisi terlebih dahulu!"
                tvHargaTermurah.requestFocus()
                return
            }
            if (mauJualTehkayuaro.isEmpty()) {
                tvMauJualTehkayuaro.error = "Mohon diisi terlebih dahulu!"
                tvMauJualTehkayuaro.requestFocus()
                return
            }
            if (jikaTidak.isEmpty()) {
                tvJikaTidak.error = "Mohon diisi terlebih dahulu!"
                tvJikaTidak.requestFocus()
                return
            }
            if (bantuan.isEmpty()) {
                tvBantuan.error = "Mohon diisi terlebih dahulu!"
                tvBantuan.requestFocus()
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
                    "$fileName.jpg",
                    addedTime)
            viewModel.insert(survey)
        }
        Handler().postDelayed({
            showAlert(true, "Sukses!... \nData Survey \nberhasil ditambahkan!")
            Toast.makeText(this, getString(R.string.save_data_success), Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1000)
    }

    private fun showAlert(check: Boolean, message: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_alert)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvMessage = dialog.findViewById(R.id.tv_message) as TextView
        tvMessage.text = message

        val btnClose = dialog.findViewById(R.id.iv_close) as ImageView
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        val lottie = dialog.findViewById(R.id.lottie_dialog) as LottieAnimationView
        if(check){
            lottie.setAnimation("success.json")
        }

        dialog.show()
    }
}