package uk.jinhy.libraryreservationserver.presentation.controller

import org.springframework.http.ResponseEntity
import uk.jinhy.libraryreservationserver.presentation.dto.ReservationListResponse

interface ReservationController {
    fun getReservationList(): ResponseEntity<ReservationListResponse>
}
