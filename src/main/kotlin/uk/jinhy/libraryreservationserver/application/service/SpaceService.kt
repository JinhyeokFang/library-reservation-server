package uk.jinhy.libraryreservationserver.application.service

import org.springframework.stereotype.Service
import uk.jinhy.libraryreservationserver.application.dto.SpaceDto

@Service
interface SpaceService {
    fun getSpaceList(): List<SpaceDto>
}
