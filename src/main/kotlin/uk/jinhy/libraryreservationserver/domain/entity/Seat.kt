package uk.jinhy.libraryreservationserver.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "seat")
class Seat(
    code: Int,
    name: String,
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
}
