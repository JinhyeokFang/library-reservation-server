package uk.jinhy.libraryreservationserver.application.service

import org.springframework.stereotype.Service
import uk.jinhy.libraryreservationserver.application.dto.ReservationDetailsDto
import uk.jinhy.libraryreservationserver.application.dto.ReservationInfoDto
import uk.jinhy.libraryreservationserver.application.dto.ReservationListDto
import uk.jinhy.libraryreservationserver.domain.entity.Reservation
import uk.jinhy.libraryreservationserver.domain.repository.ReservationRepository
import uk.jinhy.libraryreservationserver.global.config.logger
import uk.jinhy.libraryreservationserver.infrastructure.LibraryReservationClient
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class ReservationServiceImpl(
    private val librarySeatsClient: LibraryReservationClient,
    private val reservationRepository: ReservationRepository,
) : ReservationService {
    private val log by logger

    override fun getReservationList(): ReservationListDto {
        val cachedSeats = reservationRepository.findAll().toList().filterNotNull()
        
        if (cachedSeats.isNotEmpty()) {
            val cachedReservationInfoList = cachedSeats.map { reservation ->
                val details = if (reservation.checkInAt != null) {
                    ReservationDetailsDto(
                        checkInTime = reservation.checkInAt.atZone(ZoneId.systemDefault()).toInstant(),
                        expireTime = reservation.expiredAt.atZone(ZoneId.systemDefault()).toInstant()
                    )
                } else {
                    null
                }
                
                ReservationInfoDto(
                    code = reservation.seatCode.toInt(),
                    name = reservation.name,
                    isPcSeat = reservation.isPcSeat,
                    details = details
                )
            }
            
            val totalCount = cachedReservationInfoList.size
            val occupiedCount = cachedReservationInfoList.count { it.details != null }
            val availableCount = totalCount - occupiedCount
            
            return ReservationListDto(
                seats = cachedReservationInfoList,
                totalCount = totalCount,
                occupiedCount = occupiedCount,
                availableCount = availableCount
            )
        }
        
        val allSeats = mutableListOf<ReservationInfoDto>()
        
        for (roomId in 1..22) {
            try {
                val response = librarySeatsClient.getReservationList(roomId)
                val seats =
                    response.data.map { seat ->
                        val detailsDto = seat.seatTime?.let { seatTime ->
                            ReservationDetailsDto(
                                checkInTime = Instant.ofEpochMilli(seatTime.confirmInTime),
                                expireTime = Instant.ofEpochMilli(seatTime.expireTime),
                            )
                        }
                        
                        val seatInfo = ReservationInfoDto(
                            code = seat.code,
                            name = seat.name,
                            isPcSeat = seat.pcSeatYN == "Y",
                            details = detailsDto
                        )
                        
                        val checkInDateTime = seat.seatTime?.let {
                            LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(it.confirmInTime),
                                ZoneId.systemDefault()
                            )
                        }
                        
                        val expireDateTime = seat.seatTime?.let {
                            LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(it.expireTime),
                                ZoneId.systemDefault()
                            )
                        } ?: LocalDateTime.now().plusMinutes(30)
                        
                        try {
                            reservationRepository.save(
                                Reservation(
                                    seatCode = seat.code.toLong(),
                                    name = seat.name,
                                    expiredAt = expireDateTime,
                                    checkInAt = checkInDateTime,
                                    isPcSeat = seat.pcSeatYN == "Y"
                                )
                            )
                        } catch (e: Exception) {
                            log.error("좌석 캐싱 실패: ${seat.code}, ${e.message}")
                        }
                        
                        seatInfo
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
            availableCount = availableCount,
        )
    }
}
