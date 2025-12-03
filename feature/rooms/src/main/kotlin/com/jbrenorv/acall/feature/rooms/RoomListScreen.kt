package com.jbrenorv.acall.feature.rooms

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
fun RoomListScreen(
    openRoom: () -> Unit
) {
    Box(
        modifier = Modifier.background(
            color = Color.Gray
        ),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text("RoomListScreen")
            Button(onClick = openRoom) {
                Text("Open room")
            }
        }
    }
}
