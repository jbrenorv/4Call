package com.jbrenorv.acall.core.system.model

import android.telecom.DisconnectCause
import androidx.core.telecom.CallEndpointCompat

sealed interface RoomCommand {
    val roomId: String

    data class Activate(
        override val roomId: String
    ) : RoomCommand

    data class Disconnect(
        override val roomId: String,
        val cause: DisconnectCause
    ) : RoomCommand

    data class SetEndpoint(
        override val roomId: String,
        val endpoint: CallEndpointCompat
    ) : RoomCommand
}
