package uk.jinhy.libraryreservationserver.infrastructure.dto

data class LibraryReservationListDto(
    val code: Int,
    val status: Int,
    val message: String,
    val data: List<LibraryReservationInfoDto>,
    val success: Boolean
)
