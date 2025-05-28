package uk.jinhy.libraryreservationserver.domain.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.jinhy.libraryreservationserver.domain.entity.Space

@Repository
interface SpaceRepository : CrudRepository<Space, Long> {
    override fun findAll(): List<Space>
}
