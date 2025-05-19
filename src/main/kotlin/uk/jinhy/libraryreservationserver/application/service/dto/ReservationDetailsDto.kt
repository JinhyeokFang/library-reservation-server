package uk.jinhy.libraryreservationserver.application.service.dto

import java.time.Instant

data class ReservationDetailsDto(
    val checkInTime: Instant,
    val expireTime: Instant
)
