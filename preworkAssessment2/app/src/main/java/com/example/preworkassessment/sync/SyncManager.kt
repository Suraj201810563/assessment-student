package com.example.preworkassessment.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.preworkassessment.repository.StudentRepository

class SyncWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: StudentRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            repository.syncPendingData()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
