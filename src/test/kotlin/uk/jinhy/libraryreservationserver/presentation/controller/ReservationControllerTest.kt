package uk.jinhy.libraryreservationserver.presentation.controller

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import uk.jinhy.libraryreservationserver.application.service.ReservationService
import uk.jinhy.libraryreservationserver.application.service.dto.ReservationDetailsDto
import uk.jinhy.libraryreservationserver.application.service.dto.ReservationInfoDto
import uk.jinhy.libraryreservationserver.application.service.dto.ReservationListDto
import uk.jinhy.libraryreservationserver.global.response.CommonResponse
import java.time.Instant

private val reservationService = mockk<ReservationService>()

@InjectMockKs
private val reservationController: ReservationController = ReservationControllerImpl(reservationService)

@ExtendWith(MockKExtension::class)
class ReservationControllerTest : BehaviorSpec({
    Given("ReservationController에서") {
        every { reservationService.getReservationList() } returns createMockResponse()

        When("getReservationList()를 호출하면") {
            val result = reservationController.getReservationList()

            Then("응답이 성공적으로 반환되어야 한다") {
                result.statusCode.value() shouldBe 200
            }

            Then("성공 상태가 true여야 한다") {
                result.body!!.success shouldBe true
            }

            Then("좌석 정보가 올바르게 반환되어야 한다") {
                val reservationList = result.body!!.data!!
                reservationList.totalCount shouldBe 2
                reservationList.occupiedCount shouldBe 1
                reservationList.availableCount shouldBe 1
                reservationList.seats.size shouldBe 2
            }

            Then("빈 좌석 정보가 올바르게 반환되어야 한다") {
                val emptySeat = result.body!!.data!!.seats.first { it.code == 1 }
                emptySeat.name shouldBe "1"
                emptySeat.isPcSeat shouldBe false
                emptySeat.details shouldBe null
            }

            Then("예약된 좌석 정보가 올바르게 반환되어야 한다") {
                val reservedSeat = result.body!!.data!!.seats.first { it.code == 6 }
                reservedSeat.name shouldBe "6"
                reservedSeat.isPcSeat shouldBe false
                reservedSeat.details!!.checkInTime shouldBe Instant.ofEpochMilli(1747534727000)
                reservedSeat.details!!.expireTime shouldBe Instant.ofEpochMilli(1747549127000)
            }
        }
    }
})

private fun createMockResponse(): ReservationListDto {
    return ReservationListDto(
        seats = listOf(
            ReservationInfoDto(
                code = 1,
                name = "1",
                isPcSeat = false,
                details = null
            ),
            ReservationInfoDto(
                code = 6,
                name = "6",
                isPcSeat = false,
                details = ReservationDetailsDto(
                    checkInTime = Instant.ofEpochMilli(1747534727000),
                    expireTime = Instant.ofEpochMilli(1747549127000)
                )
            )
        ),
        totalCount = 2,
        occupiedCount = 1,
        availableCount = 1
    )
}
