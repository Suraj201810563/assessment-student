package com.example.preworkassessment.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.preworkassessment.model.ScoreCard
import com.example.preworkassessment.model.Student

@Database(entities = [Student::class, ScoreCard::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun studentDao(): StudentDao
    abstract fun scoreCardDao(): ScoreCardDao
}