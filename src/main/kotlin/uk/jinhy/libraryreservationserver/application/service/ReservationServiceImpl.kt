package uk.jinhy.libraryreservationserver.application.service

import org.springframework.stereotype.Service
import uk.jinhy.libraryreservationserver.application.service.dto.ReservationDetailsDto
import uk.jinhy.libraryreservationserver.application.service.dto.ReservationInfoDto
import uk.jinhy.libraryreservationserver.application.service.dto.ReservationListDto
import uk.jinhy.libraryreservationserver.global.config.logger
import uk.jinhy.libraryreservationserver.infrastructure.LibraryReservationClient
import java.time.Instant

@Service
class ReservationServiceImpl(
    private val librarySeatsClient: LibraryReservationClient
) : ReservationService {
    private val log by logger

    override fun getReservationList(): ReservationListDto {
        val allSeats = mutableListOf<ReservationInfoDto>()
        for (roomId in 1..22) {
            try {
                val response = librarySeatsClient.getReservationList(roomId)
                val seats = response.data.map { seat ->
                    ReservationInfoDto(
                        code = seat.code,
                        name = seat.name,
                        isPcSeat = seat.pcSeatYN == "Y",
                        details = seat.seatTime?.let { seatTime ->
                            ReservationDetailsDto(
                                checkInTime = Instant.ofEpochMilli(seatTime.confirmInTime),
                                expireTime = Instant.ofEpochMilli(seatTime.expireTime)
                            )
                        }
                    )
                }
                
                allSeats.addAll(seats)
            } catch (e: Exception) {
                log.error(e.message, e)
                throw e
            }
        }
        
        val totalCount = allSeats.size
        val occupiedCount = allSeats.count { it.details != null }
        val availableCount = totalCount - occupiedCount
        
        return ReservationListDto(
            seats = allSeats.toList(),
            totalCount = totalCount,
            occupiedCount = occupiedCount,
            availableCount = availableCount
        )
    }
}
