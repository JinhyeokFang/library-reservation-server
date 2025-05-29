package uk.jinhy.libraryreservationserver.infrastructure.batch

import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component
import uk.jinhy.libraryreservationserver.domain.entity.Reservation
import uk.jinhy.libraryreservationserver.domain.repository.ReservationRepository
import uk.jinhy.libraryreservationserver.global.config.logger
import uk.jinhy.libraryreservationserver.infrastructure.LibraryReservationClient
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

@Component
class ReservationCacheJobTasklet(
    private val libraryReservationClient: LibraryReservationClient,
    private val reservationRepository: ReservationRepository,
) : Tasklet {
    private val log by logger

    override fun execute(
        contribution: StepContribution,
        chunkContext: ChunkContext,
    ): RepeatStatus {
        log.info("좌석 정보 캐싱 작업 시작")

        val executor = Executors.newFixedThreadPool(22)

        try {
            val futures =
                (1..22).map { roomId ->
                    CompletableFuture.supplyAsync({
                        runCatching {
                            val response = libraryReservationClient.getReservationList(roomId)
                            response.data.map { seat ->
                                val checkInDateTime =
                                    seat.seatTime?.let {
                                        LocalDateTime.ofInstant(
                                            Instant.ofEpochMilli(it.confirmInTime),
                                            ZoneId.systemDefault(),
                                        )
                                    }

                                val expireDateTime =
                                    seat.seatTime?.let {
                                        LocalDateTime.ofInstant(
                                            Instant.ofEpochMilli(it.expireTime),
                                            ZoneId.systemDefault(),
                                        )
                                    }

                                Reservation(
                                    seatCode = seat.code.toLong(),
                                    name = seat.name,
                                    expiredAt = expireDateTime,
                                    checkInAt = checkInDateTime,
                                    isPcSeat = seat.pcSeatYN == "Y",
                                )
                            }
                        }.getOrElse { e ->
                            log.error("룸 ID $roomId 데이터 조회 실패: ${e.message}", e)
                            emptyList()
                        }
                    }, executor)
                }

            val allReservations =
                CompletableFuture.allOf(*futures.toTypedArray())
                    .thenApply {
                        futures.flatMap { it.get() }
                    }.get()

            clearExistingData()
            reservationRepository.saveAll(allReservations)
            log.info("좌석 정보 캐싱 작업 완료: ${allReservations.size}개 좌석 캐싱됨")
        } catch (e: Exception) {
            log.error("좌석 정보 캐싱 작업 실패: ${e.message}", e)
        } finally {
            executor.shutdown()
        }

        return RepeatStatus.FINISHED
    }

    private fun clearExistingData() {
        reservationRepository.deleteAll()
        log.info("기존 캐시 데이터 삭제 완료")
    }
}
