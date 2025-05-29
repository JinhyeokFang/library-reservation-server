package uk.jinhy.libraryreservationserver.global.config

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.transaction.PlatformTransactionManager
import uk.jinhy.libraryreservationserver.infrastructure.batch.ReservationCacheJobTasklet

@Configuration
@EnableScheduling
class BatchConfig(
    private val reservationCacheJobTasklet: ReservationCacheJobTasklet,
) {
    @Bean
    fun reservationCacheJob(
        jobRepository: JobRepository,
        reservationCacheStep: Step,
    ): Job {
        return JobBuilder("reservationCacheJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(reservationCacheStep)
            .build()
    }

    @Bean
    fun reservationCacheStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
    ): Step {
        return StepBuilder("reservationCacheStep", jobRepository)
            .tasklet(reservationCacheJobTasklet, transactionManager)
            .build()
    }
}
