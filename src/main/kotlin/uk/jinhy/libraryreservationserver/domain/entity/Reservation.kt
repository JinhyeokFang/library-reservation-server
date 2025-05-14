package uk.jinhy.libraryreservationserver.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.time.LocalDateTime

@RedisHash("reservation")
class Reservation (
    @Id
    val seatCode: Long,
    val expiredAt: LocalDateTime,
)
