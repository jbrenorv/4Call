package com.jbrenorv.a4call.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ChatScreen(
    goBack: () -> Unit
) {
    Box(
        modifier = Modifier.background(
            color = Color.Gray
        ),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text("ChatScreen")
            Button(onClick = goBack) {
                Text("Go back")
            }
        }
    }
}
