package com.example.preworkassessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.preworkassessment.data.AppDataBase
import com.example.preworkassessment.model.Student
import com.example.preworkassessment.network.RetrofitClient
import com.example.preworkassessment.repository.StudentRepository
import com.example.preworkassessment.screen.AddScoreCardForm
import com.example.preworkassessment.screen.AddStudentForm
import com.example.preworkassessment.screen.ScoreCardListScreen
import com.example.preworkassessment.screen.StudentListScreen
import com.example.preworkassessment.ui.StudentViewModel
import com.example.preworkassessment.ui.StudentViewModelFactory
import com.example.preworkassessment.ui.theme.PreworkAssessmentTheme

class MainActivity : ComponentActivity() {

    private val viewModel: StudentViewModel by viewModels {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java,
            "student-db"
        ).build()

        val apiService = RetrofitClient.apiService
        val repository = StudentRepository(db, apiService)
        StudentViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PreworkAssessmentTheme {
                var showAddForm by remember { mutableStateOf(false) }
                var selectedStudent by remember { mutableStateOf<Student?>(null) }
                var addScoreCardFor by remember { mutableStateOf<Student?>(null) }

                when {
                    showAddForm -> {
                        AddStudentForm(onSave = {
                            viewModel.addStudent(it)
                            showAddForm = false
                        })
                    }
                    addScoreCardFor != null -> {
                        AddScoreCardForm(
                            studentId = addScoreCardFor!!.id,
                            onSave = {
                                viewModel.addScoreCard(it)
                                addScoreCardFor = null
                            }
                        )
                    }
                    selectedStudent != null -> {
                        ScoreCardListScreen(
                            student = selectedStudent!!,
                            viewModel = viewModel,
                            onBack = { selectedStudent = null }
                        )
                    }
                    else -> {
                        Column {
                            StudentListScreen(
                                viewModel = viewModel,
                                onStudentClick = { student -> selectedStudent = student },
                                onAddScoreCard = { student -> addScoreCardFor = student }
                            )
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                onClick = { showAddForm = true }
                            ) {
                                Text("Add New Student")
                            }
                        }
                    }
                }
            }
        }
    }
}




