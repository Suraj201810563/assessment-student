package com.example.preworkassessment.network

import com.example.preworkassessment.model.ScoreCard
import com.example.preworkassessment.model.Student
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("students")
    suspend fun upsertStudent(@Body student: Student): ApiResponse

    @POST("scorecards")
    suspend fun upsertScoreCard(@Body scoreCard: ScoreCard): ApiResponse


    data class ApiResponse(val success: Boolean, val message: String)
}