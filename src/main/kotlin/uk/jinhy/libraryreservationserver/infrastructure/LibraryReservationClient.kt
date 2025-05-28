package uk.jinhy.libraryreservationserver.infrastructure

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import uk.jinhy.libraryreservationserver.infrastructure.dto.LibraryReservationListDto

@FeignClient(name = "kyonggiUnivLibrarySeats", url = "https://libgate.kyonggi.ac.kr")
interface LibraryReservationClient {
    @GetMapping("/libraries/seats/{roomId}")
    fun getReservationList(
        @PathVariable roomId: Int,
    ): LibraryReservationListDto
}
