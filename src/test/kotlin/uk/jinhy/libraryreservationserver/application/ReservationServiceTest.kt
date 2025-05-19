package uk.jinhy.libraryreservationserver.application.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import uk.jinhy.libraryreservationserver.infrastructure.KyonggiUnivLibrarySeatsClient
import uk.jinhy.libraryreservationserver.infrastructure.KyonggiUnivLibrarySeatsClient.LibrarySeatResponse
import uk.jinhy.libraryreservationserver.infrastructure.KyonggiUnivLibrarySeatsClient.Seat
import uk.jinhy.libraryreservationserver.infrastructure.KyonggiUnivLibrarySeatsClient.SeatTime

class ReservationServiceTest : BehaviorSpec({
    val librarySeatsClient = mockk<KyonggiUnivLibrarySeatsClient>()
    val reservationService = ReservationService(librarySeatsClient)
    
    Given("ReservationService에서") {
        every { librarySeatsClient.getSeats(1) } returns createMockResponse()
        every { librarySeatsClient.getSeats(any()) } returns LibrarySeatResponse(0, 0, "", emptyList(), true)
        
        When("getAllSeats()를 호출하면") {
            val result = reservationService.getAllSeats()
            
            Then("좌석 정보가 올바르게 반환되어야 한다") {
                result.totalCount shouldBe 2
                result.occupiedCount shouldBe 1
                result.availableCount shouldBe 1
                result.seats.size shouldBe 2
            }
            
            Then("빈 좌석 정보가 올바르게 반환되어야 한다") {
                val emptySeat = result.seats.first { it.code == 1 }
                emptySeat.name shouldBe "1"
                emptySeat.reservationInfo shouldBe null
            }
            
            Then("예약된 좌석 정보가 올바르게 반환되어야 한다") {
                val reservedSeat = result.seats.first { it.code == 6 }
                reservedSeat.name shouldBe "6"
                reservedSeat.reservationInfo shouldBe ReservationService.ReservationInfo(
                    id = 587481,
                    seatId = 6,
                    checkInTime = reservedSeat.reservationInfo!!.checkInTime,
                    expireTime = reservedSeat.reservationInfo!!.expireTime
                )
            }
        }
    }
})

private fun createMockResponse(): LibrarySeatResponse {
    return LibrarySeatResponse(
        code = 1,
        status = 200,
        message = "SUCCESS",
        data = listOf(
            Seat(
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
            Seat(
                code = 6,
                name = "6",
                state = 0,
                xpos = 310,
                ypos = 470,
                width = 35,
                height = 80,
                textSize = 12,
                seatTime = SeatTime(
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