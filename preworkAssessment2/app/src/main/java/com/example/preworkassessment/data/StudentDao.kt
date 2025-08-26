package com.example.preworkassessment.data

import androidx.room.*
import com.example.preworkassessment.model.Student
import com.example.preworkassessment.model.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {

    @Query("SELECT * FROM students ORDER BY fullName")
    fun getAllStudents(): Flow<List<Student>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("SELECT * FROM students WHERE syncStatus != :synced")
    suspend fun getPendingStudents(synced: SyncStatus = SyncStatus.SYNCED): List<Student>
}