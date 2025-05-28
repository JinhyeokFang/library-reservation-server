package uk.jinhy.libraryreservationserver.infrastructure.dto

data class LibraryReservationDetailsDto(
    val idx: Long,
    val seatId: Int,
    val mySeat: Boolean,
    val confirmInTime: Long,
    val expireTime: Long,
)
