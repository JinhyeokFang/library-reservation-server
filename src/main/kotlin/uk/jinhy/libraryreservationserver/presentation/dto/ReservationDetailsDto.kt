package uk.jinhy.libraryreservationserver.presentation.dto

import java.time.Instant

data class ReservationDetailsDto(
    val expireTime: Instant,
    val checkInTime: Instant,
)
