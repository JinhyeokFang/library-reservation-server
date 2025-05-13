package uk.jinhy.libraryreservationserver.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "space")
class Space (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
)
