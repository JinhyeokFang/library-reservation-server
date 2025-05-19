package uk.jinhy.libraryreservationserver.presentation.controller

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import uk.jinhy.libraryreservationserver.application.service.ReservationService
import uk.jinhy.libraryreservationserver.application.service.ReservationService.ReservationInfo
import uk.jinhy.libraryreservationserver.application.service.ReservationService.SeatInfo
import uk.jinhy.libraryreservationserver.application.service.ReservationService.SeatItem
import java.time.Instant

@WebMvcTest(ReservationController::class)
class ReservationControllerTest(
    private val mockMvc: MockMvc,
) : BehaviorSpec() {
    override fun extensions() = listOf(SpringExtension)
    
    @MockkBean
    private lateinit var reservationService: ReservationService
    
    init {
        Given("ReservationController에서") {
            every { reservationService.getAllSeats() } returns createMockSeatInfo()
            
            When("/api/v1/seats으로 요청하면") {
                val resultActions = mockMvc.perform(
                    get("/api/v1/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                
                Then("좌석 정보가 올바르게 반환되어야 한다") {
                    resultActions
                        .andDo(print())
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.totalCount").value(2))
                        .andExpect(jsonPath("$.occupiedCount").value(1))
                        .andExpect(jsonPath("$.availableCount").value(1))
                        .andExpect(jsonPath("$.seats.length()").value(2))
                }
                
                Then("빈 좌석 정보가 올바르게 반환되어야 한다") {
                    resultActions
                        .andExpect(jsonPath("$.seats[0].code").value(1))
                        .andExpect(jsonPath("$.seats[0].name").value("1"))
                        .andExpect(jsonPath("$.seats[0].reservationInfo").doesNotExist())
                }
                
                Then("예약된 좌석 정보가 올바르게 반환되어야 한다") {
                    resultActions
                        .andExpect(jsonPath("$.seats[1].code").value(6))
                        .andExpect(jsonPath("$.seats[1].name").value("6"))
                        .andExpect(jsonPath("$.seats[1].reservationInfo").exists())
                        .andExpect(jsonPath("$.seats[1].reservationInfo.seatId").value(6))
                }
            }
        }
    }
    
    private fun createMockSeatInfo(): SeatInfo {
        val now = Instant.now()
        return SeatInfo(
            seats = listOf(
                SeatItem(
                    code = 1,
                    name = "1",
                    state = 0,
                    xPosition = 215,
                    yPosition = 270,
                    isPcSeat = false,
                    reservationInfo = null
                ),
                SeatItem(
                    code = 6,
                    name = "6",
                    state = 0,
                    xPosition = 310,
                    yPosition = 470,
                    isPcSeat = false,
                    reservationInfo = ReservationInfo(
                        id = 587481,
                        seatId = 6,
                        checkInTime = now,
                        expireTime = now.plusSeconds(3600)
                    )
                )
            ),
            totalCount = 2,
            occupiedCount = 1,
            availableCount = 1
        )
    }
} 