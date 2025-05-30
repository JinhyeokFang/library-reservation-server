package uk.jinhy.libraryreservationserver.application.dto

data class ReservationInfoDto(
    val code: Int,
    val name: String,
    val isPcSeat: Boolean,
    val details: ReservationDetailsDto?,
)
