package uk.jinhy.libraryreservationserver.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "space")
class Space (
    name: String,
    floor: Int,
    seats: MutableList<Seat> = mutableListOf(),
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false, unique = true)
    var name = name
    protected set

    @Column(nullable = false)
    var floor = floor
    protected set

    @Column(nullable = false)
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    var seats: MutableList<Seat> = seats
    protected set
}

