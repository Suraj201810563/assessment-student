package com.example.preworkassessment.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.preworkassessment.model.ScoreCard
import com.example.preworkassessment.model.Student
import com.example.preworkassessment.model.SyncStatus
import com.example.preworkassessment.ui.StudentViewModel
import java.util.*

@Composable
fun StudentListScreen(
    viewModel: StudentViewModel,
    onStudentClick: (Student) -> Unit,
    onAddScoreCard: (Student) -> Unit
) {
    val students by viewModel.students.collectAsState()
    LazyColumn {
        items(students) { student ->
            StudentItem(
                student = student,
                onRetry = { viewModel.retrySync() },
                onClick = { onStudentClick(student) },
                onAddScoreCard = { onAddScoreCard(student) }
            )
        }
    }
}

@Composable
fun StudentItem(
    student: Student,
    onRetry: () -> Unit,
    onClick: () -> Unit,
    onAddScoreCard: () -> Unit // new lambda
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(student.fullName, fontWeight = FontWeight.Bold)
            Text("Class: ${student.studentClass}")
        }
        IconButton(onClick = onAddScoreCard) {
            Icon(Icons.Default.Add, contentDescription = "Add ScoreCard")
        }
        when (student.syncStatus) {
            SyncStatus.SYNCED -> Icon(Icons.Default.Done, contentDescription = "Synced", tint = Color.Green)
            SyncStatus.PENDING -> CircularProgressIndicator(modifier = Modifier.size(20.dp))
            SyncStatus.FAILED -> IconButton(onClick = onRetry) {
                Icon(Icons.Default.Refresh, contentDescription = "Retry", tint = Color.Red)
            }
        }
    }
}

@Composable
fun AddStudentForm(onSave: (Student) -> Unit) {
    var fullName by remember { mutableStateOf("") }
    var studentClass by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Full Name") })
        TextField(value = studentClass, onValueChange = { studentClass = it }, label = { Text("Class") })
        Button(onClick = {
            val newStudent = Student(
                id = UUID.randomUUID().toString(),
                fullName = fullName,
                studentClass = studentClass,
                gender = "Not set", // Set as appropriate
                schoolId = "Not set", // Set as appropriate
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                syncStatus = SyncStatus.PENDING
            )
            onSave(newStudent)
        }) {
            Text("Save Student")
        }
    }
}

@Composable
fun ScoreCardListScreen(
    student: Student,
    viewModel: StudentViewModel,
    onBack: () -> Unit
) {
    val scoreCards by viewModel.getScoreCards(student.id).collectAsState()
    Column {
        Text("ScoreCards for ${student.fullName}", style = MaterialTheme.typography.h6)
        LazyColumn {
            items(scoreCards) { scoreCard ->
                Text("${scoreCard.subject}: ${scoreCard.score}")
                // Add scorecard-specific sync status, edit/delete if needed
            }
        }
        AddScoreCardForm(studentId = student.id, onSave = { viewModel.addScoreCard(it) })
        Button(onClick = onBack, modifier = Modifier.padding(8.dp)) {
            Text("Back")
        }
    }
}

@Composable
fun AddScoreCardForm(studentId: String, onSave: (ScoreCard) -> Unit) {
    var subject by remember { mutableStateOf("") }
    var score by remember { mutableStateOf("") }
    Column(Modifier.padding(8.dp)) {
        TextField(
            value = subject,
            onValueChange = { subject = it },
            label = { Text("Subject") }
        )
        TextField(
            value = score,
            onValueChange = { score = it },
            label = { Text("Score") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Button(
            onClick = {
                if (subject.isNotBlank() && score.isNotBlank()) {
                    val newScoreCard = ScoreCard(
                        id = UUID.randomUUID().toString(),
                        studentId = studentId,
                        subject = subject,
                        score = score.toIntOrNull() ?: 0,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis(),
                        syncStatus = SyncStatus.PENDING
                    )
                    onSave(newScoreCard)
                    subject = ""
                    score = ""
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Save Score")
        }
    }
}



