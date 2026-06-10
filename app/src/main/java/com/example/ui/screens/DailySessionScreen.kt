package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.model.Lesson
import com.example.ui.LessonState
import com.example.ui.TutorViewModel
import com.example.ui.components.CodeExampleCard
import com.example.ui.components.ConceptHeaderCard
import com.example.ui.components.KeyIdeasCard
import com.example.ui.components.QuizQuestionCard
import com.example.ui.components.QuizSubmitButton
import com.example.ui.components.ScoreBanner
import com.example.ui.components.SessionErrorState
import com.example.ui.components.SessionLoadingState
import com.example.ui.components.SessionProgressHeader

@Composable
fun DailySessionScreen(
    viewModel: TutorViewModel,
    modifier: Modifier = Modifier,
    onNavigateToSetup: () -> Unit = {},
    onNavigateToTutor: () -> Unit = {},
    onNavigateToProgress: () -> Unit = {}
) {
    val lessonState by viewModel.lessonState.collectAsState()
    val scrollState = rememberScrollState()
    val isTodayCompleted by viewModel.isTodaySessionCompleted.collectAsState()
    val isSubmittedGlobal by viewModel.isQuizSubmitted.collectAsState()
    val isTopicCompleted by viewModel.isTopicCompleted.collectAsState()
    val currentCycleInfo by viewModel.currentCycleInfo.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        if (isTodayCompleted && !isSubmittedGlobal) {
            TodayCompletedView(
                viewModel = viewModel,
                isTopicCompleted = isTopicCompleted,
                topicName = currentCycleInfo?.topic ?: "",
                onNavigateToSetup = onNavigateToSetup,
                onNavigateToTutor = onNavigateToTutor,
                onNavigateToProgress = onNavigateToProgress
            )
        } else {
            when (val state = lessonState) {
                is LessonState.Loading -> {
                    SessionLoadingState()
                }
                is LessonState.Error -> {
                    SessionErrorState(
                        message = state.message,
                        onRetry = { viewModel.retryLoadLesson() },
                        onNavigateToSetup = onNavigateToSetup
                    )
                }
                is LessonState.Success -> {
                    val lesson = state.lesson
                    val selectedAnswers by viewModel.quizSelectedAnswers.collectAsState()
                    val isSubmitted by viewModel.isQuizSubmitted.collectAsState()
                    val quizScore by viewModel.quizScore.collectAsState()
                    val isSubmitting by viewModel.isQuizSubmitting.collectAsState()
                    val streak by viewModel.streakCount.collectAsState()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(16.dp)
                    ) {
                        SessionProgressHeader(
                            cycleInfo = lesson.cycleInfo,
                            lesson = lesson,
                            streak = streak,
                            isSubmitted = isSubmitted
                        )

                        ConceptHeaderCard(conceptTitle = lesson.conceptTitle)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Lesson Core content
                        Text(
                            text = stringResource(R.string.concept_understanding),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        Text(
                            text = lesson.shortExplanation,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        KeyIdeasCard(bulletPoints = lesson.bulletPoints)

                        Spacer(modifier = Modifier.height(16.dp))

                        CodeExampleCard(example = lesson.simpleExample)

                        Spacer(modifier = Modifier.height(24.dp))

                        // Quiz Section
                        Text(
                            text = stringResource(R.string.daily_assessment),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        lesson.quizQuestions.forEachIndexed { qIdx, question ->
                            QuizQuestionCard(
                                question = question,
                                questionIndex = qIdx,
                                totalQuestions = lesson.quizQuestions.size,
                                selectedAnswerIndex = selectedAnswers[question.id],
                                isSubmitted = isSubmitted,
                                onSelectAnswer = { optIdx ->
                                    viewModel.selectQuizAnswer(question.id, optIdx)
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Final assessment actions
                        if (!isSubmitted) {
                            val isAllAnswered = selectedAnswers.size == lesson.quizQuestions.size
                            QuizSubmitButton(
                                isAllAnswered = isAllAnswered,
                                isSubmitting = isSubmitting,
                                onSubmit = { viewModel.submitQuizAnswers(lesson) }
                            )
                        } else {
                            ScoreBanner(
                                score = quizScore,
                                totalQuestions = lesson.quizQuestions.size,
                                nextDayNumber = lesson.dayNumber + 1
                            )
                        }

                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TodayCompletedView(
    viewModel: TutorViewModel,
    isTopicCompleted: Boolean = false,
    topicName: String = "",
    onNavigateToSetup: () -> Unit,
    onNavigateToTutor: () -> Unit,
    onNavigateToProgress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsState()
    val streak by viewModel.streakCount.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isTopicCompleted) {
                    // Topic completed — celebration state
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "\u00a1Topic completado!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Felicitaciones, terminaste el tema \"${topicName.ifBlank { profile?.topic ?: "" }}\".",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Eleg\u00ed un tema nuevo para seguir aprendiendo.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.outline
                    )
                } else {
                    // Normal daily completion
                    Icon(
                        imageVector = Icons.Default.AssignmentTurnedIn,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "\u00a1Todo listo por hoy!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Ya completaste tu lecci\u00f3n diaria sobre \"${profile?.topic ?: ""}\".",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "\uD83D\uDD25", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Racha actual: $streak d\u00eda${if (streak != 1) "s" else ""}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Una nueva lecci\u00f3n se desbloquear\u00e1 ma\u00f1ana.",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = if (isTopicCompleted) "\u00bfQu\u00e9 quer\u00e9s hacer ahora?" else "\u00bfQu\u00e9 quer\u00e9s hacer ahora?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isTopicCompleted) {
            // Topic completed: primary action is picking a new topic
            Button(
                onClick = onNavigateToSetup,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Schedule, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Elegir nuevo tema", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onNavigateToProgress,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Default.Timeline, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver mi Progreso", fontWeight = FontWeight.Bold)
            }
        } else {
            Button(
                onClick = onNavigateToTutor,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Chat, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Chatear con el Tutor IA", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onNavigateToProgress,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Default.Timeline, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver mi Progreso y Errores", fontWeight = FontWeight.Bold)
            }
        }
    }
}
