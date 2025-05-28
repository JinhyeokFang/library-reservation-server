package uk.jinhy.libraryreservationserver.domain

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.context.annotation.Import
import uk.jinhy.libraryreservationserver.config.TestContainersConfig
import uk.jinhy.libraryreservationserver.domain.entity.Reservation
import uk.jinhy.libraryreservationserver.domain.repository.ReservationRepository
import java.time.LocalDateTime

@DataRedisTest
@Import(TestContainersConfig::class)
class ReservationTest
    @Autowired
    constructor(
        private val reservationRepository: ReservationRepository,
    ) : BehaviorSpec({
            Given("ReservationRepository의") {
                val reservation1 =
                    Reservation(
                        seatCode = 1,
                        name = "좌석1",
                        expiredAt = LocalDateTime.now().plusHours(1),
                        checkInAt = LocalDateTime.now(),
                        isPcSeat = false,
                    )
                val reservation2 =
                    Reservation(
                        seatCode = 2,
                        name = "좌석2",
                        expiredAt = LocalDateTime.now().plusHours(2),
                        checkInAt = LocalDateTime.now(),
                        isPcSeat = true,
                    )
                val reservationList = listOf(reservation1, reservation2)
                When("saveAll()를 호출하면") {
                    val savedReservation = reservationRepository.saveAll(reservationList)
                    Then("데이터베이스에 잘 저장되어야한다.") {
                        savedReservation shouldNotBe null
                    }
                }
            }
        })
