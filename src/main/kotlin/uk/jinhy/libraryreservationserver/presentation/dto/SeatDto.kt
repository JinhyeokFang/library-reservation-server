package uk.jinhy.libraryreservationserver.presentation.dto

import java.time.Instant

data class SeatResponseDto(
    val seats: List<SeatDto>,
    val totalCount: Int,
    val occupiedCount: Int,
    val availableCount: Int
)

data class SeatDto(
    val code: Int,
    val name: String,
    val state: Int,
    val xPosition: Int,
    val yPosition: Int,
    val isPcSeat: Boolean,
    val reservationInfo: SeatReservationInfoDto?
)

data class SeatReservationInfoDto(
    val id: Long,
    val seatId: Int,
    val checkInTime: Instant,
    val expireTime: Instant
) 