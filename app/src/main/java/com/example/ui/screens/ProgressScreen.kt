package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.MAX_LESSONS
import com.example.ui.TutorViewModel
import com.example.ui.components.CompletedTopicsSection
import com.example.ui.components.CompletionCard
import com.example.ui.components.EmptyGoalState
import com.example.ui.components.MistakesSection
import com.example.ui.components.StreakCard
import com.example.ui.components.TrackProgressOverview

@Composable
fun ProgressScreen(
    viewModel: TutorViewModel,
    modifier: Modifier = Modifier,
    onNavigateToSetup: () -> Unit = {}
) {
    val currentProfile by viewModel.userProfile.collectAsState()
    val progressList by viewModel.allProgress.collectAsState()
    val streak by viewModel.streakCount.collectAsState()
    val mistakes by viewModel.allMistakes.collectAsState()
    val apiProgress by viewModel.apiProgress.collectAsState()

    val scrollState = rememberScrollState()
    var showResetDialog by remember { mutableStateOf(false) }

    // Fetch API progress when screen becomes visible
    LaunchedEffect(Unit) {
        viewModel.refreshProgressFromApi()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        if (currentProfile == null) {
            EmptyGoalState(
                onNavigateToSetup = onNavigateToSetup
            )
        } else {
            val topic = currentProfile?.topic ?: "General Topic"
            val totalLessonsInTrack = MAX_LESSONS
            val completedCount = progressList.filter { it.topic.equals(topic, ignoreCase = true) }.size

            // Use API progress cycle data if available
            val apiCycle = apiProgress?.cycle

            // Header Overview — uses current_topic from API if available
            TrackProgressOverview(
                topic = topic,
                completedCount = completedCount,
                totalLessons = totalLessonsInTrack,
                apiCycle = apiCycle,
                conceptMastery = apiProgress?.conceptMastery,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StreakCard(
                    streak = streak,
                    modifier = Modifier.weight(1f)
                )
                CompletionCard(
                    completedCount = completedCount,
                    modifier = Modifier.weight(1f)
                )
            }

            // Completed Topics section (from API)
            val completedTopics = apiProgress?.completedTopics
            if (!completedTopics.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
            }
            CompletedTopicsSection(
                completedTopics = completedTopics,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mistakes and Review Unit
            MistakesSection(
                mistakes = mistakes,
                onDeleteMistake = { id -> viewModel.deleteMistake(id) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Action section
            Button(
                onClick = { showResetDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("reset_progress_button"),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.RotateLeft,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.clear_track),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(stringResource(R.string.reset_dialog_title)) },
            text = { Text(stringResource(R.string.reset_dialog_text)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearHistory()
                        showResetDialog = false
                    },
                    modifier = Modifier.testTag("confirm_reset")
                ) {
                    Text(stringResource(R.string.confirm_reset), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}
