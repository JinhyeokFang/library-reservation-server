package uk.jinhy.libraryreservationserver.infrastructure.batch

import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher

@ExtendWith(MockKExtension::class)
class ReservationCacheSchedulerTest : BehaviorSpec({
    Given("ReservationCacheScheduler에서") {
        val jobLauncher = mockk<JobLauncher>()
        val reservationCacheJob = mockk<Job>()
        val scheduler =
            ReservationCacheScheduler(
                jobLauncher = jobLauncher,
                reservationCacheJob = reservationCacheJob,
            )

        every { jobLauncher.run(any(), any()) } returns mockk()

        When("scheduleReservationCacheJob()을 호출하면") {
            scheduler.scheduleReservationCacheJob()

            Then("작업을 실행해야 한다") {
                verify(exactly = 1) { jobLauncher.run(reservationCacheJob, any<JobParameters>()) }
            }
        }
    }
})
