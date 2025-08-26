package com.example.preworkassessment.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(

    @PrimaryKey val id: String,
    val fullName: String,
    val studentClass: String,
    val gender: String,
    val schoolId: String,
    val createdAt: Long,
    val updatedAt: Long,
    val syncStatus: SyncStatus
)
