package com.jbrenorv.acall.core.system.model

import android.telecom.DisconnectCause
import androidx.core.telecom.CallAttributesCompat
import androidx.core.telecom.CallEndpointCompat
import androidx.core.telecom.CallException

data class TelecomRoomData(
    val roomId: String,
    val attributes: CallAttributesCompat,
    val state: TelecomRoomState,
    val isAudioMuted: Boolean,
    val currentEndpoint: CallEndpointCompat?,
    val availableEndpoints: List<CallEndpointCompat>?,
    val callException: CallException?,
    val disconnectCause: DisconnectCause?,
) {
    fun isMine() = attributes.direction == CallAttributesCompat.DIRECTION_OUTGOING
}
