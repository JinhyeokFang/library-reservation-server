package uk.jinhy.libraryreservationserver.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "seat")
class Seat(
    space: Space,
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Seat::class, optional = false)
    var space = space
    protected set
}
