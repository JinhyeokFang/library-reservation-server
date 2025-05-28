package uk.jinhy.libraryreservationserver.application.service

import org.springframework.stereotype.Service
import uk.jinhy.libraryreservationserver.application.service.dto.ReservationListDto

@Service
interface ReservationService {
    fun getReservationList(): ReservationListDto
}
