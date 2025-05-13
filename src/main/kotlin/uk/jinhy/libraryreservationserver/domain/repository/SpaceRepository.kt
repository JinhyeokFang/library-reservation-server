package uk.jinhy.libraryreservationserver.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.jinhy.libraryreservationserver.domain.entity.Space

interface SpaceRepository: JpaRepository<Space, Long> {
}