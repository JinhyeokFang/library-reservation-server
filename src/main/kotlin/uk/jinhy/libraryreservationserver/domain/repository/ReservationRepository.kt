package uk.jinhy.libraryreservationserver.domain.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.jinhy.libraryreservationserver.domain.entity.Reservation

@Repository
interface ReservationRepository: CrudRepository<Reservation, Long> {
}
