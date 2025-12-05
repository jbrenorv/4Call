package com.jbrenorv.acall.feature.rooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jbrenorv.acall.core.database.repository.RoomRepository
import com.jbrenorv.acall.core.model.Room
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RoomListViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {
    val uiState: StateFlow<RoomListUiState> =
        roomRepository.getRooms()
            .map<List<Room>, RoomListUiState>(RoomListUiState::Success)
            .onStart { emit(RoomListUiState.Loading) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = RoomListUiState.Loading,
            )
}
