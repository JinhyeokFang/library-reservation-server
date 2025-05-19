package uk.jinhy.libraryreservationserver.application.service

import org.springframework.stereotype.Service
import uk.jinhy.libraryreservationserver.config.logger
import uk.jinhy.libraryreservationserver.infrastructure.KyonggiUnivLibrarySeatsClient
import java.time.Instant

@Service
class ReservationService(
    private val librarySeatsClient: KyonggiUnivLibrarySeatsClient
) {
    private val log by logger

    fun getAllSeats(): SeatInfo {
        val allSeats = mutableListOf<SeatItem>()
        log.info("Looking up all seats...")
        for (roomId in 1..22) {
            try {
                val response = librarySeatsClient.getSeats(roomId)
                log.info(response.toString())

                val seats = response.data.map { seat ->
                    SeatItem(
                        code = seat.code,
                        name = seat.name,
                        state = seat.state,
                        xPosition = seat.xpos,
                        yPosition = seat.ypos,
                        isPcSeat = seat.pcSeatYN == "Y",
                        reservationInfo = seat.seatTime?.let { seatTime ->
                            ReservationInfo(
                                id = seatTime.idx,
                                seatId = seatTime.seatId,
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
        val occupiedCount = allSeats.count { it.reservationInfo != null }
        val availableCount = totalCount - occupiedCount
        
        return SeatInfo(
            seats = allSeats,
            totalCount = totalCount,
            occupiedCount = occupiedCount,
            availableCount = availableCount
        )
    }
    
    data class SeatInfo(
        val seats: List<SeatItem>,
        val totalCount: Int,
        val occupiedCount: Int,
        val availableCount: Int
    )

    data class SeatItem(
        val code: Int,
        val name: String,
        val state: Int,
        val xPosition: Int,
        val yPosition: Int,
        val isPcSeat: Boolean,
        val reservationInfo: ReservationInfo?
    )

    data class ReservationInfo(
        val id: Long,
        val seatId: Int,
        val checkInTime: Instant,
        val expireTime: Instant
    )
}