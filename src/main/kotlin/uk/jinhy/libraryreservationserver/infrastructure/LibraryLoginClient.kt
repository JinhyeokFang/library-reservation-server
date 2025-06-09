package uk.jinhy.libraryreservationserver.infrastructure

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import uk.jinhy.libraryreservationserver.global.config.FeignConfig
import uk.jinhy.libraryreservationserver.infrastructure.dto.LibraryLoginRequest

@FeignClient(name = "libraryLoginClient", url = "https://library.kyonggi.ac.kr", configuration = [FeignConfig::class])
interface LibraryLoginClient {
    @PostMapping("/login")
    fun login(
        @RequestBody request: LibraryLoginRequest,
    ): ResponseEntity<Void>

    @GetMapping("/relation/seat")
    fun getLoginToken(
        @RequestHeader("Cookie") jsessionId: String,
    ): ResponseEntity<String>

    @PostMapping("/login_library")
    fun loginToSeatSystem(
        @RequestBody formData: Map<String, String>,
        @RequestHeader("Cookie") jsessionId: String,
        @RequestHeader("Content-Type") contentType: String = "application/x-www-form-urlencoded",
        @RequestHeader("Accept") accept: String =
            "text/html,application/xhtml+xml,application/xml;q=0.9," +
                "image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
        @RequestHeader("Accept-Language") acceptLanguage: String = "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7",
        @RequestHeader("Cache-Control") cacheControl: String = "no-cache",
        @RequestHeader("Pragma") pragma: String = "no-cache",
        @RequestHeader("Origin") origin: String = "https://library.kyonggi.ac.kr",
        @RequestHeader("Referer") referer: String = "https://library.kyonggi.ac.kr/",
    ): ResponseEntity<Void>
}
