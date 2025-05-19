package uk.jinhy.libraryreservationserver.application.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import uk.jinhy.libraryreservationserver.application.service.dto.ReservationDetailsDto
import uk.jinhy.libraryreservationserver.infrastructure.LibraryReservationClient
import uk.jinhy.libraryreservationserver.infrastructure.dto.LibraryReservationDetailsDto
import uk.jinhy.libraryreservationserver.infrastructure.dto.LibraryReservationInfoDto
import uk.jinhy.libraryreservationserver.infrastructure.dto.LibraryReservationListDto

private val librarySeatsClient = mockk<LibraryReservationClient>()

@InjectMockKs
private val reservationService = ReservationService(librarySeatsClient)

@ExtendWith(MockKExtension::class)
class ReservationServiceTest : BehaviorSpec({
    Given("ReservationService에서") {
        every { librarySeatsClient.getReservationList(any()) } returns LibraryReservationListDto(
            0,
            0,
            "",
            emptyList(),
            true
        )
        every { librarySeatsClient.getReservationList(1) } returns createMockResponse()

        When("getAllSeats()를 호출하면") {
            val result = reservationService.getReservationList()

            Then("좌석 정보가 올바르게 반환되어야 한다") {
                result.totalCount shouldBe 2
                result.occupiedCount shouldBe 1
                result.availableCount shouldBe 1
                result.seats.size shouldBe 2
            }

            Then("빈 좌석 정보가 올바르게 반환되어야 한다") {
                val emptySeat = result.seats.first { it.code == 1 }
                emptySeat.name shouldBe "1"
                emptySeat.details shouldBe null
            }

            Then("예약된 좌석 정보가 올바르게 반환되어야 한다") {
                val reservedSeat = result.seats.first { it.code == 6 }
                reservedSeat.name shouldBe "6"
                reservedSeat.details shouldBe ReservationDetailsDto(
                    checkInTime = reservedSeat.details!!.checkInTime,
                    expireTime = reservedSeat.details!!.expireTime
                )
            }
        }
    }
})

private fun createMockResponse(): LibraryReservationListDto {
    return LibraryReservationListDto(
        code = 1,
        status = 200,
        message = "SUCCESS",
        data = listOf(
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
                pcSeatYN = "N"
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
                seatTime = LibraryReservationDetailsDto(
                    idx = 587481,
                    seatId = 6,
                    mySeat = false,
                    confirmInTime = 1747534727000,
                    expireTime = 1747549127000
                ),
                pcSeatYN = "N"
            )
        ),
        success = true
    )
}
