package com.jbrenorv.a4call.feature.chat_list

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
fun ChatListScreen(
    openChat: () -> Unit
) {
    Box(
        modifier = Modifier.background(
            color = Color.Gray
        ),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text("ChatListScreen")
            Button(onClick = openChat) {
                Text("Open chat")
            }
        }
    }
}
