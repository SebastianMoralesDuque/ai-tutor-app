package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.MAX_LESSONS
import com.example.ui.TutorViewModel
import com.example.ui.components.SaveGoalButton
import com.example.ui.components.TimeCommitmentCard
import com.example.ui.components.TopicInputCard

@Composable
fun GoalSetupScreen(
    viewModel: TutorViewModel,
    modifier: Modifier = Modifier,
    onSaveSuccess: () -> Unit = {}
) {
    val currentProfile by viewModel.userProfile.collectAsState()
    val isTopicLocked by viewModel.isTopicLocked.collectAsState()
    val topicSuggestions by viewModel.topicSuggestions.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.fetchSuggestions()
    }

    var topicInput by remember(currentProfile) {
        mutableStateOf(currentProfile?.topic ?: "")
    }
    var studyMinutes by remember(currentProfile) {
        mutableStateOf(currentProfile?.dailyStudyMinutes ?: 20)
    }
    var isSaving by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Hero Header
        Icon(
            imageVector = Icons.Default.School,
            contentDescription = stringResource(R.string.learning_tutor_logo),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(72.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (currentProfile == null) stringResource(R.string.setup_goal_title) else stringResource(R.string.refine_goal_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(R.string.goal_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        TopicInputCard(
            topicInput = topicInput,
            onTopicChange = { topicInput = it },
            topicSuggestions = topicSuggestions
        )

        Spacer(modifier = Modifier.height(16.dp))

        TimeCommitmentCard(
            studyMinutes = studyMinutes,
            onMinutesChange = { studyMinutes = it }
        )

        if (isTopicLocked) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(0.85f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.6f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text(
                    text = "Complet\u00e1 las ${MAX_LESSONS} lecciones antes de cambiar de tema.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        SaveGoalButton(
            isSaving = isSaving,
            enabled = topicInput.isNotBlank() && !isSaving && !isTopicLocked,
            isExistingProfile = currentProfile != null,
            activeTopic = currentProfile?.topic ?: "",
            activeMinutes = currentProfile?.dailyStudyMinutes ?: 0,
            onSave = {
                if (topicInput.isNotBlank() && !isSaving) {
                    isSaving = true
                    viewModel.saveGoals(topicInput.trim(), studyMinutes) {
                        isSaving = false
                        onSaveSuccess()
                    }
                }
            }
        )
    }
}
