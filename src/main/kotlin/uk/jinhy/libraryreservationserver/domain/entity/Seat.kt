package uk.jinhy.libraryreservationserver.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "seat")
class Seat(
    code: Int,
    name: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
