package uk.jinhy.libraryreservationserver.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@RedisHash("reservation")
class Reservation(
    @Id
    val seatCode: Long,
    val expiredAt: LocalDateTime?,
    val checkInAt: LocalDateTime?,
    val isPcSeat: Boolean,
    val name: String,
    @TimeToLive(unit = TimeUnit.SECONDS)
    val ttl: Long = 60,
)
