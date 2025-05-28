package uk.jinhy.libraryreservationserver.presentation.dto

data class ReservationItemDto(
    val code: Int,
    val name: String,
    val isPcSeat: Boolean,
    val details: ReservationDetailsDto?,
)
