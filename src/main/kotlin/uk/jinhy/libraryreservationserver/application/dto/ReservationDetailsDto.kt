package uk.jinhy.libraryreservationserver.application.dto

import java.time.Instant

data class ReservationDetailsDto(
    val checkInTime: Instant,
    val expireTime: Instant,
)
