package com.example.preworkassessment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preworkassessment.model.ScoreCard
import com.example.preworkassessment.model.Student
import com.example.preworkassessment.repository.StudentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: StudentRepository)  : ViewModel(){

    val students = repository.getAllStudents().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun retrySync() {
        viewModelScope.launch {
            repository.syncPendingData()
        }
    }
    fun addStudent(student: Student) {
        viewModelScope.launch {
            repository.insertOrUpdateStudent(student)
        }
    }

    // Fetch scorecards for selected student
    fun getScoreCards(studentId: String): StateFlow<List<ScoreCard>> =
        repository.getScoreCards(studentId).stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addScoreCard(scoreCard: ScoreCard) = viewModelScope.launch {
        repository.insertOrUpdateScoreCard(scoreCard)
    }
}