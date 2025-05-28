package uk.jinhy.libraryreservationserver.presentation.dto

data class ReservationListResponse(
    val seats: List<ReservationItemDto>,
    val totalCount: Int,
    val occupiedCount: Int,
    val availableCount: Int,
)
