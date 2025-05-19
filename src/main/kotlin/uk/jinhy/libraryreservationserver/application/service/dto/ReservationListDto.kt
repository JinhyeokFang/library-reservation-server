package uk.jinhy.libraryreservationserver.application.service.dto

data class ReservationListDto(
    val seats: List<ReservationInfoDto>,
    val totalCount: Int,
    val occupiedCount: Int,
    val availableCount: Int
)
