package uk.jinhy.libraryreservationserver.infrastructure.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import uk.jinhy.libraryreservationserver.global.config.logger

@Component
class ReservationCacheScheduler(
    private val jobLauncher: JobLauncher,
    private val reservationCacheJob: Job,
) {
    private val log by logger

    @Scheduled(fixedRate = 45000)
    fun scheduleReservationCacheJob() {
        try {
            val jobParameters: JobParameters =
                JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters()

            jobLauncher.run(reservationCacheJob, jobParameters)
            log.info("예약 캐시 작업 스케줄링 성공")
        } catch (e: Exception) {
            log.error("예약 캐시 작업 스케줄링 실패: ${e.message}", e)
        }
    }
}
