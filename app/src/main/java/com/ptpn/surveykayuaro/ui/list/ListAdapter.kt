package com.ptpn.surveykayuaro.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ptpn.surveykayuaro.R
import com.ptpn.surveykayuaro.data.source.remote.response.SurveyResponse
import com.ptpn.surveykayuaro.databinding.ItemSurveysBinding
import java.util.*
import kotlin.collections.ArrayList

class ListAdapter(private val listSurveys: List<SurveyResponse>, val context: Context) : RecyclerView.Adapter<ListAdapter.ListViewHolder>(), Filterable {

    private lateinit var onItemClickCallback: OnItemClickCallback
    var filterList = ArrayList<SurveyResponse>()

    init{
        filterList = listSurveys as ArrayList<SurveyResponse>
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: SurveyResponse)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(private val binding: ItemSurveysBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(survey: SurveyResponse) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemSurveysBinding = ItemSurveysBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return ListViewHolder(itemSurveysBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val survey = filterList[position]
        holder.bind(survey)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(filterList[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = filterList.size

    override fun getFilter(): Filter {
        return customFilter
    }

    private val customFilter = object: Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val charSearch = constraint.toString()
            filterList = if (charSearch.isEmpty()){
                listSurveys as ArrayList<SurveyResponse>
            }else{
                val resultList = ArrayList<SurveyResponse>()
                for (row in listSurveys) {
                    if (row.namaKedai!!.toLowerCase(Locale.ROOT).contains(constraint.toString().toLowerCase(Locale.ROOT))

                    ) {
                        resultList.add(row)
                    }
                }
                resultList
            }
            val results = FilterResults()
            results.values = filterList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filterList = results?.values as ArrayList<SurveyResponse>
            notifyDataSetChanged()
        }
    }
}