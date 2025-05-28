package uk.jinhy.libraryreservationserver.infrastructure.dto

data class LibraryReservationInfoDto(
    val code: Int,
    val name: String,
    val state: Int,
    val xpos: Int,
    val ypos: Int,
    val width: Int,
    val height: Int,
    val textSize: Int?,
    val seatTime: LibraryReservationDetailsDto?,
    val pcSeatYN: String,
)
