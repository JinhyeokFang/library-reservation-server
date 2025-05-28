package uk.jinhy.libraryreservationserver.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "space")
class Space(
    name: String,
    floor: Int,
    seats: MutableList<Seat> = mutableListOf(),
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
