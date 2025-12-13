package com.jbrenorv.acall.feature.rooms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jbrenorv.acall.feature.rooms.component.RoomCard
import com.jbrenorv.acall.feature.rooms.component.RoomListEmptyState
import com.jbrenorv.acall.feature.rooms.component.RoomListLoadingState

@Composable
internal fun RoomListRoute(
    openRoom: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RoomListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RoomListScreen(
        openRoom = openRoom,
        createRoom = viewModel::createRoom,
        modifier = modifier,
        uiState = uiState
    )
}

@Composable
internal fun RoomListScreen(
    openRoom: () -> Unit,
    createRoom: () -> Unit,
    modifier: Modifier = Modifier,
    uiState: RoomListUiState
) {
    Box {
        when (uiState) {
            RoomListUiState.Loading -> RoomListLoadingState(modifier)
            is RoomListUiState.Success -> if (uiState.rooms.isNotEmpty()) {
                RoomList(
                    modifier = modifier,
                    openRoom = openRoom,
                    uiState = uiState
                )
            } else {
                RoomListEmptyState(modifier)
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            onClick = createRoom
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create room"
            )
        }
    }
}

@Composable
private fun RoomList(
    modifier: Modifier = Modifier,
    openRoom: () -> Unit,
    uiState: RoomListUiState.Success
) {
    LazyColumn(modifier) {
        items(items = uiState.rooms) { room ->
            RoomCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                openRoom = openRoom,
                room = room
            )
        }
    }
}
