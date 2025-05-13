package uk.jinhy.libraryreservationserver.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "seat")
class Seat(
    code: Int,
    name: String,
    status: SeatStatus,
    expiredAt: LocalDateTime?,
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Seat::class, optional = false)
    var space: Space? = null
    protected set

    @Column(nullable = false, unique = true)
    var code = code
    protected set

    @Column(nullable = false)
    var name = name
    protected set

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status = status
    protected set

    @Column(nullable = true)
    var expiredAt = expiredAt
    protected set
}
