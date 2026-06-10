package com.example.ui.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

/**
 * Parses **bold** markdown syntax within standard Text composables.
 */
@Composable
fun parseBoldTextStyle(input: String, defaultColor: Color) = buildAnnotatedString {
    val boldToken = "**"
    var startIndex = 0
    while (true) {
        val tokenStart = input.indexOf(boldToken, startIndex)
        if (tokenStart == -1) {
            withStyle(style = SpanStyle(color = defaultColor)) {
                append(input.substring(startIndex))
            }
            break
        }
        if (tokenStart > startIndex) {
            withStyle(style = SpanStyle(color = defaultColor)) {
                append(input.substring(startIndex, tokenStart))
            }
        }
        val tokenEnd = input.indexOf(boldToken, tokenStart + boldToken.length)
        if (tokenEnd == -1) {
            withStyle(style = SpanStyle(color = defaultColor)) {
                append(input.substring(tokenStart))
            }
            break
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Black, color = defaultColor)) {
            append(input.substring(tokenStart + boldToken.length, tokenEnd))
        }
        startIndex = tokenEnd + boldToken.length
    }
}
