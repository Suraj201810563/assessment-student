package com.example.preworkassessment.data

import androidx.room.*
import com.example.preworkassessment.model.ScoreCard
import com.example.preworkassessment.model.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreCardDao {

    @Query("SELECT * FROM scorecards WHERE studentId = :studentId ORDER BY subject ASC")
    fun getScoreCardsForStudent(studentId: String): Flow<List<ScoreCard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScoreCard(scoreCard: ScoreCard)

    @Delete
    suspend fun deleteScoreCard(scoreCard: ScoreCard)

    @Query("SELECT * FROM scorecards WHERE syncStatus != :synced")
    suspend fun getPendingScoreCards(synced: SyncStatus = SyncStatus.SYNCED): List<ScoreCard>
}
