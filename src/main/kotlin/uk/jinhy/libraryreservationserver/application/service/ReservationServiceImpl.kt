package uk.jinhy.libraryreservationserver.application.service

import org.springframework.stereotype.Service
import uk.jinhy.libraryreservationserver.application.dto.ReservationDetailsDto
import uk.jinhy.libraryreservationserver.application.dto.ReservationInfoDto
import uk.jinhy.libraryreservationserver.application.dto.ReservationListDto
import uk.jinhy.libraryreservationserver.domain.repository.ReservationRepository
import uk.jinhy.libraryreservationserver.global.config.logger
import java.time.ZoneId

@Service
class ReservationServiceImpl(
    private val reservationRepository: ReservationRepository,
) : ReservationService {
    private val log by logger

    override fun getReservationList(): ReservationListDto {
        val cachedSeats = reservationRepository.findAll().toList()

        if (cachedSeats.isEmpty()) {
            log.warn("캐시된 좌석 정보가 없습니다. 배치 작업이 아직 실행되지 않았을 수 있습니다.")
            return ReservationListDto(
                seats = emptyList(),
                totalCount = 0,
                occupiedCount = 0,
                availableCount = 0,
            )
        }

        val cachedReservationInfoList =
            cachedSeats.map { reservation ->
                val details =
                    if (reservation.checkInAt != null && reservation.expiredAt != null) {
                        ReservationDetailsDto(
                            checkInTime = reservation.checkInAt.atZone(ZoneId.systemDefault()).toInstant(),
                            expireTime = reservation.expiredAt.atZone(ZoneId.systemDefault()).toInstant(),
                        )
                    } else {
                        null
                    }

                ReservationInfoDto(
                    code = reservation.seatCode.toInt(),
                    name = reservation.name,
                    isPcSeat = reservation.isPcSeat,
                    details = details,
                )
            }

        val totalCount = cachedReservationInfoList.size
        val occupiedCount = cachedReservationInfoList.count { it.details != null }
        val availableCount = totalCount - occupiedCount

        return ReservationListDto(
            seats = cachedReservationInfoList,
            totalCount = totalCount,
            occupiedCount = occupiedCount,
            availableCount = availableCount,
        )
    }
}
