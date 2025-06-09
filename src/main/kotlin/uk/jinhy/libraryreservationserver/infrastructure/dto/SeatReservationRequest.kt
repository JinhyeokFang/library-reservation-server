package uk.jinhy.libraryreservationserver.infrastructure.dto

data class SeatReservationRequest(
    val seatId: Long,
    val time: Int = 240,
)
