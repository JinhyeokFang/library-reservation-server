package uk.jinhy.libraryreservationserver.presentation.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.jinhy.libraryreservationserver.application.service.ReservationService
import uk.jinhy.libraryreservationserver.presentation.dto.ReservationDetailsDto
import uk.jinhy.libraryreservationserver.presentation.dto.ReservationItemDto
import uk.jinhy.libraryreservationserver.presentation.dto.ReservationListResponse

@RestController
@RequestMapping("/api/v1/seats")
class ReservationController(
    private val reservationService: ReservationService
) {
    @GetMapping
    fun getReservationList(): ResponseEntity<ReservationListResponse> {
        val seatInfo = reservationService.getReservationList()
        
        val response = ReservationListResponse(
            seats = seatInfo.seats.map {
                ReservationItemDto(
                    code = it.code,
                    name = it.name,
                    isPcSeat = it.isPcSeat,
                    details = it.details?.let { details ->
                        ReservationDetailsDto(
                            expireTime = details.expireTime,
                            checkInTime = details.checkInTime,
                        )
                    }
                )
            },
            totalCount = seatInfo.totalCount,
            occupiedCount = seatInfo.occupiedCount,
            availableCount = seatInfo.availableCount
        )
        
        return ResponseEntity.ok(response)
    }
}
