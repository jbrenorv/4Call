package com.jbrenorv.acall.feature.rooms

import com.jbrenorv.acall.core.model.Room

sealed interface RoomListUiState {
    data object Loading : RoomListUiState

    data class Success(
        val rooms: List<Room>
    ) : RoomListUiState
}
