package uk.jinhy.libraryreservationserver.application.service

import org.springframework.stereotype.Service
import uk.jinhy.libraryreservationserver.application.dto.SeatDto
import uk.jinhy.libraryreservationserver.application.dto.SpaceDto
import uk.jinhy.libraryreservationserver.domain.repository.SpaceRepository

@Service
class SpaceServiceImpl(
    private val spaceRepository: SpaceRepository
) : SpaceService {
    override fun getSpaceList(): List<SpaceDto> {
        val spaces = spaceRepository.findAll()
        return spaces.map {
            SpaceDto(
                name = it.name,
                floor = it.floor,
                seats = it.seats.map {
                    SeatDto(
                        id = 0
                    )
                }
            )
        }
    }
}
