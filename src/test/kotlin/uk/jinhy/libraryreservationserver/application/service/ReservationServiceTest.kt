package uk.jinhy.libraryreservationserver.application.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import uk.jinhy.libraryreservationserver.domain.entity.Reservation
import uk.jinhy.libraryreservationserver.domain.repository.ReservationRepository
import uk.jinhy.libraryreservationserver.infrastructure.dto.LibraryReservationDetailsDto
import uk.jinhy.libraryreservationserver.infrastructure.dto.LibraryReservationInfoDto
import uk.jinhy.libraryreservationserver.infrastructure.dto.LibraryReservationListDto
import java.time.LocalDateTime
import java.time.ZoneOffset

@ExtendWith(MockKExtension::class)
class ReservationServiceTest : BehaviorSpec({
    Given("캐시된 데이터가 없을 때") {
        val reservationRepository = mockk<ReservationRepository>()
        val reservationService = ReservationServiceImpl(reservationRepository)

        every { reservationRepository.findAll() } returns emptyList()

        When("getReservationList()을 호출하면") {
            val result = reservationService.getReservationList()

            Then("빈 리스트를 반환해야 한다") {
                result.totalCount shouldBe 0
                result.occupiedCount shouldBe 0
                result.availableCount shouldBe 0
                result.seats.size shouldBe 0
            }
        }
    }

    Given("캐시된 데이터가 있을 때") {
        val reservationRepository = mockk<ReservationRepository>()
        val reservationService = ReservationServiceImpl(reservationRepository)

        val expireDateTime = LocalDateTime.ofEpochSecond(1747549127, 0, ZoneOffset.UTC)
        val checkInDateTime = LocalDateTime.ofEpochSecond(1747534727, 0, ZoneOffset.UTC)
        val cachedReservations =
            listOf(
                Reservation(
                    seatCode = 6L,
                    name = "6",
                    expiredAt = expireDateTime,
                    checkInAt = checkInDateTime,
                    isPcSeat = true,
                ),
                Reservation(
                    seatCode = 1L,
                    name = "1",
                    expiredAt = null,
                    checkInAt = null,
                    isPcSeat = false,
                ),
            )

        every { reservationRepository.findAll() } returns cachedReservations

        When("getReservationList()을 호출하면") {
            val result = reservationService.getReservationList()

            Then("캐시된 데이터를 반환해야 한다") {
                result.seats.size shouldBe 2
                result.totalCount shouldBe 2
                result.occupiedCount shouldBe 1
                result.availableCount shouldBe 1

                val cachedOccupiedSeat = result.seats.first { it.code == 6 }
                cachedOccupiedSeat.name shouldBe "6"
                cachedOccupiedSeat.isPcSeat shouldBe true
                cachedOccupiedSeat.details shouldNotBe null
                cachedOccupiedSeat.details?.checkInTime shouldNotBe null

                val cachedEmptySeat = result.seats.first { it.code == 1 }
                cachedEmptySeat.name shouldBe "1"
                cachedEmptySeat.isPcSeat shouldBe false
                cachedEmptySeat.details shouldBe null
            }
        }
    }
})

private fun createMockResponse(): LibraryReservationListDto {
    return LibraryReservationListDto(
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
}
