package com.ptpn.surveykayuaro.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ptpn.surveykayuaro.data.source.local.entity.SurveyEntity

@Database(entities = [SurveyEntity::class], version = 1)
abstract class SurveyDatabase: RoomDatabase() {
    abstract fun surveyDao(): SurveyDao

    companion object {
        @Volatile
        private var INSTANCE: SurveyDatabase? = null

        fun getInstance(context: Context): SurveyDatabase =
                INSTANCE ?: synchronized(this) {
                    Room.databaseBuilder(
                            context.applicationContext,
                            SurveyDatabase::class.java,
                            "survey_database.db"
                    ).build().apply {
                        INSTANCE = this
                    }
                }
    }
}