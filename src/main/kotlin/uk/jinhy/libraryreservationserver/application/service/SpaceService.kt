package uk.jinhy.libraryreservationserver.application.service

import uk.jinhy.libraryreservationserver.application.service.dto.SeatDto
import uk.jinhy.libraryreservationserver.application.service.dto.SpaceDto
import uk.jinhy.libraryreservationserver.domain.repository.SpaceRepository

class SpaceService(
    private val spaceRepository: SpaceRepository
) {
    fun getSpaceList(): List<SpaceDto> {
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
