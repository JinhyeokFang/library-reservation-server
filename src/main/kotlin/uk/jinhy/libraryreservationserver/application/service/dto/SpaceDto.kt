package uk.jinhy.libraryreservationserver.application.service.dto

data class SpaceDto (
    val name: String,
    val floor: Int,
    val seats: List<SeatDto>
)
