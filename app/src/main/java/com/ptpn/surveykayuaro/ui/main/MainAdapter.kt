package com.ptpn.surveykayuaro.ui.main

import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.databinding.ItemSurveysLocalBinding
import java.util.*

class MainAdapter(private var listSurvey: List<SurveyEntity>) : RecyclerView.Adapter<MainAdapter.SurveyViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: SurveyEntity)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class SurveyViewHolder(private val binding: ItemSurveysLocalBinding) : RecyclerView.ViewHolder(binding.root){
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(survey: SurveyEntity) {
            with(binding) {
                tvItemKedai.text = survey.namaKedai
                tvItemNarasumber.text = survey.namaNarasumber
                tvItemAlamat.text = survey.alamatKedai
                tvItemAddedTime.text = survey.addedTime

                val bytes: ByteArray = Base64.getDecoder().decode(survey.image)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imgItemImage.setImageBitmap(bitmap)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyViewHolder {
        val itemSurveysLocalBinding = ItemSurveysLocalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SurveyViewHolder(itemSurveysLocalBinding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SurveyViewHolder, position: Int) {
        val survey = listSurvey[position]
        holder.bind(survey)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listSurvey[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listSurvey.size
}