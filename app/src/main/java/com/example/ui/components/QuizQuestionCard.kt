package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.model.QuizQuestion

@Composable
fun QuizQuestionCard(
    question: QuizQuestion,
    questionIndex: Int,
    totalQuestions: Int,
    selectedAnswerIndex: Int?,
    isSubmitted: Boolean,
    onSelectAnswer: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val isQuestionCorrect = selectedAnswerIndex == question.correctAnswerIndex

    Text(
        text = stringResource(R.string.question_of, questionIndex + 1, totalQuestions),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.outline
    )

    Spacer(modifier = Modifier.height(4.dp))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = if (isSubmitted) 2.dp else 1.dp,
            color = when {
                !isSubmitted -> MaterialTheme.colorScheme.outline
                isQuestionCorrect -> Color(0xFF4CAF50)
                else -> Color(0xFFF44336)
            }
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = question.question,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Options List
            question.options.forEachIndexed { optIdx, option ->
                val isSelected = selectedAnswerIndex == optIdx
                val optionContainerColor = when {
                    !isSubmitted && isSelected -> MaterialTheme.colorScheme.primaryContainer
                    isSubmitted && optIdx == question.correctAnswerIndex -> Color(0xFFE8F5E9)
                    isSubmitted && isSelected && !isQuestionCorrect -> Color(0xFFFFEBEE)
                    else -> Color.Transparent
                }

                val optionBorderColor = when {
                    !isSubmitted && isSelected -> MaterialTheme.colorScheme.primary
                    isSubmitted && optIdx == question.correctAnswerIndex -> Color(0xFF4CAF50)
                    isSubmitted && isSelected && !isQuestionCorrect -> Color(0xFFF44336)
                    else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(optionContainerColor)
                        .border(
                            1.dp,
                            optionBorderColor,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable(enabled = !isSubmitted) {
                            onSelectAnswer(optIdx)
                        }
                        .padding(14.dp)
                        .testTag("question_${question.id}_option_$optIdx"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = {
                            if (!isSubmitted) {
                                onSelectAnswer(optIdx)
                            }
                        },
                        enabled = !isSubmitted,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected || (isSubmitted && optIdx == question.correctAnswerIndex)) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.weight(1f)
                    )

                    if (isSubmitted) {
                        if (optIdx == question.correctAnswerIndex) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Correct",
                                tint = Color(0xFF4CAF50)
                            )
                        } else if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Incorrect",
                                tint = Color(0xFFF44336)
                            )
                        }
                    }
                }
            }

            // Interactive explanation review for incorrect answers
            AnimatedVisibility(visible = isSubmitted) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.review_explanation),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = question.explanation,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}
