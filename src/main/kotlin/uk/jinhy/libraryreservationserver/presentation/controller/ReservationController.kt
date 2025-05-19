package uk.jinhy.libraryreservationserver.presentation.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.jinhy.libraryreservationserver.application.service.ReservationService
import uk.jinhy.libraryreservationserver.presentation.dto.SeatDto
import uk.jinhy.libraryreservationserver.presentation.dto.SeatReservationInfoDto
import uk.jinhy.libraryreservationserver.presentation.dto.SeatResponseDto

@RestController
@RequestMapping("/api/v1/seats")
class ReservationController(
    private val reservationService: ReservationService
) {
    
    @GetMapping
    fun getAllSeats(): ResponseEntity<SeatResponseDto> {
        val seatInfo = reservationService.getAllSeats()
        
        val response = SeatResponseDto(
            seats = seatInfo.seats.map { 
                SeatDto(
                    code = it.code,
                    name = it.name,
                    state = it.state,
                    xPosition = it.xPosition,
                    yPosition = it.yPosition,
                    isPcSeat = it.isPcSeat,
                    reservationInfo = it.reservationInfo?.let { info ->
                        SeatReservationInfoDto(
                            id = info.id,
                            seatId = info.seatId,
                            checkInTime = info.checkInTime,
                            expireTime = info.expireTime
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