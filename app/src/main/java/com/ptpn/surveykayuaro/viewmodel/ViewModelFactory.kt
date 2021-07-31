package com.ptpn.surveykayuaro.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ptpn.surveykayuaro.ui.main.MainViewModel
import com.ptpn.surveykayuaro.data.SurveyRepository
import com.ptpn.surveykayuaro.di.Injection
import com.ptpn.surveykayuaro.ui.detail.DetailViewModel
import com.ptpn.surveykayuaro.ui.form.FormViewModel
import com.ptpn.surveykayuaro.ui.list.ListViewModel

class ViewModelFactory private constructor(private val mSurveyRepository: SurveyRepository)
    : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
                instance ?: synchronized(this) {
                    ViewModelFactory(Injection.provideRepository(context)).apply { instance = this }
                }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(mSurveyRepository) as T
            }
            modelClass.isAssignableFrom(FormViewModel::class.java) -> {
                FormViewModel(mSurveyRepository) as T
            }
            modelClass.isAssignableFrom(ListViewModel::class.java) -> {
                ListViewModel(mSurveyRepository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(mSurveyRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }

    }
}