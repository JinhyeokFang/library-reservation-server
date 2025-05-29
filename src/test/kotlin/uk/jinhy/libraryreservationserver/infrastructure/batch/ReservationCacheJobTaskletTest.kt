package uk.jinhy.libraryreservationserver.infrastructure.batch

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.repeat.RepeatStatus
import uk.jinhy.libraryreservationserver.domain.entity.Reservation
import uk.jinhy.libraryreservationserver.domain.repository.ReservationRepository
import uk.jinhy.libraryreservationserver.infrastructure.LibraryReservationClient
import uk.jinhy.libraryreservationserver.infrastructure.dto.LibraryReservationDetailsDto
import uk.jinhy.libraryreservationserver.infrastructure.dto.LibraryReservationInfoDto
import uk.jinhy.libraryreservationserver.infrastructure.dto.LibraryReservationListDto

@ExtendWith(MockKExtension::class)
class ReservationCacheJobTaskletTest : BehaviorSpec({
    Given("ReservationCacheJobTasklet에서") {
        val libraryReservationClient = mockk<LibraryReservationClient>()
        val reservationRepository = mockk<ReservationRepository>()
        val tasklet =
            ReservationCacheJobTasklet(
                libraryReservationClient = libraryReservationClient,
                reservationRepository = reservationRepository,
            )
        val stepContribution = mockk<StepContribution>()
        val chunkContext = mockk<ChunkContext>()

        every { libraryReservationClient.getReservationList(any()) } returns
            LibraryReservationListDto(
                code = 1,
                status = 200,
                message = "SUCCESS",
                data =
                    listOf(
                        LibraryReservationInfoDto(
                            code = 1,
                            name = "1",
                            state = 0,
                            xpos = 215,
                            ypos = 270,
                            width = 35,
                            height = 80,
                            textSize = 12,
                            seatTime = null,
                            pcSeatYN = "N",
                        ),
                        LibraryReservationInfoDto(
                            code = 6,
                            name = "6",
                            state = 0,
                            xpos = 310,
                            ypos = 470,
                            width = 35,
                            height = 80,
                            textSize = 12,
                            seatTime =
                                LibraryReservationDetailsDto(
                                    idx = 587481,
                                    seatId = 6,
                                    mySeat = false,
                                    confirmInTime = 1747534727000,
                                    expireTime = 1747549127000,
                                ),
                            pcSeatYN = "Y",
                        ),
                    ),
                success = true,
            )
        every { reservationRepository.deleteAll() } returns Unit
        every { reservationRepository.saveAll(any<List<Reservation>>()) } returnsArgument 0

        When("execute()를 호출하면") {
            val result = tasklet.execute(stepContribution, chunkContext)

            Then("RepeatStatus.FINISHED를 반환해야 한다") {
                result shouldBe RepeatStatus.FINISHED
            }

            Then("모든 룸 ID에 대해 API를 호출해야 한다") {
                verify(exactly = 22) { libraryReservationClient.getReservationList(any()) }
            }

            Then("기존 캐시 데이터를 삭제해야 한다") {
                verify(exactly = 1) { reservationRepository.deleteAll() }
            }

            Then("새로운 데이터를 저장해야 한다") {
                verify(exactly = 1) { reservationRepository.saveAll(any<List<Reservation>>()) }
            }
        }
    }
})
