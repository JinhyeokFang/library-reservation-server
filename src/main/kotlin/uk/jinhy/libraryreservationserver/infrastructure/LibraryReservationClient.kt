package uk.jinhy.libraryreservationserver.infrastructure

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import uk.jinhy.libraryreservationserver.global.config.FeignConfig
import uk.jinhy.libraryreservationserver.infrastructure.dto.LibraryReservationListDto
import uk.jinhy.libraryreservationserver.infrastructure.dto.SeatReservationRequest
import uk.jinhy.libraryreservationserver.infrastructure.dto.SeatReservationResponse

@FeignClient(name = "libraryReservationClient", url = "https://libgate.kyonggi.ac.kr", configuration = [FeignConfig::class])
interface LibraryReservationClient {
    @GetMapping("/libraries/seats/{roomId}")
    fun getReservationList(
        @PathVariable roomId: Int,
    ): LibraryReservationListDto

    @PostMapping(value = ["/libraries/seat"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun reserveSeat(
        @RequestBody request: SeatReservationRequest,
        @RequestHeader("Cookie") cliSid: String,
        @RequestHeader("accept") accept: String = "application/json, text/plain, */*",
        @RequestHeader("cache-control") cacheControl: String = "no-cache",
        @RequestHeader("pragma") pragma: String = "no-cache",
        @RequestHeader("Referer") referer: String = "https://libgate.kyonggi.ac.kr/",
    ): ResponseEntity<SeatReservationResponse>

    @PostMapping(value = ["/libraries/leave/{seatId}"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun cancelReservation(
        @PathVariable seatId: Long,
        @RequestBody emptyBody: Map<String, String> = mapOf(),
        @RequestHeader("Cookie") cliSid: String,
        @RequestHeader("accept") accept: String = "application/json, text/plain, */*",
        @RequestHeader("cache-control") cacheControl: String = "no-cache",
        @RequestHeader("pragma") pragma: String = "no-cache",
        @RequestHeader("Referer") referer: String = "https://libgate.kyonggi.ac.kr/",
    ): ResponseEntity<SeatReservationResponse>
}
