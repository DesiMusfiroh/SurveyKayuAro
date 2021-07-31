package com.ptpn.surveykayuaro.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ptpn.surveykayuaro.R
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity
import com.ptpn.surveykayuaro.databinding.ItemSurveysBinding

class MainAdapter(private var listSurvey: List<SurveyEntity>) : RecyclerView.Adapter<MainAdapter.SurveyViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: SurveyEntity)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class SurveyViewHolder(private val binding: ItemSurveysBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(survey: SurveyEntity) {
            with(binding) {
                tvItemKedai.text = survey.namaKedai
                tvItemNarasumber.text = survey.namaNarasumber
                tvItemSurveyor.text = survey.namaSurveyor
                tvAlamatKedai.text = survey.alamatKedai
                tvAddedTime.text = survey.addedTime
                Glide.with(itemView.context)
                        .load(survey.image)
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_loading)
                                .error(R.drawable.ic_error))
                        .into(imgItemImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyViewHolder {
        val itemSurveysBinding = ItemSurveysBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SurveyViewHolder(itemSurveysBinding)
    }

    override fun onBindViewHolder(holder: SurveyViewHolder, position: Int) {
        val survey = listSurvey[position]
        holder.bind(survey)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listSurvey[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listSurvey.size
}