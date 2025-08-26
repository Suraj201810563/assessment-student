package com.example.preworkassessment.repository

import com.example.preworkassessment.data.AppDataBase
import com.example.preworkassessment.model.ScoreCard
import com.example.preworkassessment.model.Student
import com.example.preworkassessment.model.SyncStatus
import com.example.preworkassessment.network.ApiService

class StudentRepository(private val db: AppDataBase, private val api: ApiService) {

    fun getAllStudents() = db.studentDao().getAllStudents()
    fun getScoreCards(studentId: String) = db.scoreCardDao().getScoreCardsForStudent(studentId)

    suspend fun insertOrUpdateStudent(student: Student) {
        db.studentDao().insertStudent(student.copy(syncStatus = SyncStatus.PENDING, updatedAt = System.currentTimeMillis()))
    }

    suspend fun deleteStudent(student: Student) {
        db.studentDao().deleteStudent(student)
    }

    suspend fun insertOrUpdateScoreCard(scoreCard: ScoreCard) {
        db.scoreCardDao().insertScoreCard(scoreCard.copy(syncStatus = SyncStatus.PENDING, updatedAt = System.currentTimeMillis()))
    }

    suspend fun deleteScoreCard(scoreCard: ScoreCard) {
        db.scoreCardDao().deleteScoreCard(scoreCard)
    }

    suspend fun syncPendingData() {
        val pendingStudents = db.studentDao().getPendingStudents()
        for (student in pendingStudents) {
            try {
                val response = api.upsertStudent(student)
                if (response.success) {
                    db.studentDao().insertStudent(student.copy(syncStatus = SyncStatus.SYNCED))
                } else {
                    db.studentDao().insertStudent(student.copy(syncStatus = SyncStatus.FAILED))
                }
            } catch (e: Exception) {
                db.studentDao().insertStudent(student.copy(syncStatus = SyncStatus.FAILED))
            }
        }

        val pendingScoreCards = db.scoreCardDao().getPendingScoreCards()
        for (scoreCard in pendingScoreCards) {
            try {
                val response = api.upsertScoreCard(scoreCard)
                if (response.success) {
                    db.scoreCardDao().insertScoreCard(scoreCard.copy(syncStatus = SyncStatus.SYNCED))
                } else {
                    db.scoreCardDao().insertScoreCard(scoreCard.copy(syncStatus = SyncStatus.FAILED))
                }
            } catch (e: Exception) {
                db.scoreCardDao().insertScoreCard(scoreCard.copy(syncStatus = SyncStatus.FAILED))
            }
        }
    }


}