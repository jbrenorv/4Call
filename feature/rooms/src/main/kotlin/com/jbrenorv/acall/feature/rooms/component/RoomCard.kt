package com.jbrenorv.acall.feature.rooms.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jbrenorv.acall.core.model.room.Room
import com.jbrenorv.acall.core.model.user.User

@Composable
internal fun RoomCard(
    modifier: Modifier = Modifier,
    openRoom: () -> Unit,
    room: Room
) {
    Card(modifier) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            RoomCardHeader(
                room = room
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(Modifier.height(16.dp))
            RoomCardContent(
                modifier = Modifier.fillMaxWidth(),
                openRoom = openRoom,
                host = room.host,
                guest = room.guests.firstOrNull()
            )
        }
    }
}

@Composable
private fun RoomCardHeader(
    modifier: Modifier = Modifier,
    room: Room
) {
    val title = buildAnnotatedString {
        append("${room.language} ")
        withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
            append(room.languageLevel.name)
        }
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 4Call custom icon
        Icon(
            imageVector = Icons.Default.Groups,
            contentDescription = null
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title)
            Text(room.topic)
        }
        Spacer(Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null
        )
    }
}

@Composable
private fun RoomCardContent(
    modifier: Modifier = Modifier,
    openRoom: () -> Unit,
    host: User,
    guest: User? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            UserCard(
                modifier = Modifier.weight(1f),
                user = host
            )
            Spacer(Modifier.width(16.dp))
            if (guest == null) {
                EmptyUserCard(
                    modifier = Modifier.weight(1f),
                )
            } else {
                UserCard(
                    modifier = Modifier.weight(1f),
                    user = guest
                )
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = openRoom,
                    enabled = guest == null
                ) {
                    Text("Join")
                }
            }
        }
    }
}

@Composable
private fun UserCard(
    modifier: Modifier = Modifier,
    user: User
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                color = Color.Green.copy(alpha = .5f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (!user.photoUrl.isNullOrBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "User profile photo",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )
        } else {
            val letter = (user.name.firstOrNull() ?: 'A').toString()
            Text(
                text = letter.uppercase(),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun EmptyUserCard(
    modifier: Modifier = Modifier,
) {
    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(
                color = Color.Transparent,
                shape = CircleShape
            )
            .drawBehind {
                drawCircle(
                    color = Color.Black,
                    radius = size.minDimension / 2.0f,
                    style = stroke
                )
            }
    )
}
