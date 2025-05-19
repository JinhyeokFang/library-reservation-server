package uk.jinhy.libraryreservationserver.infrastructure

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "kyonggiUnivLibrarySeats", url = "https://libgate.kyonggi.ac.kr")
interface KyonggiUnivLibrarySeatsClient {
    
    @GetMapping("/libraries/seats/{roomId}")
    fun getSeats(@PathVariable roomId: Int): LibrarySeatResponse
    
    data class LibrarySeatResponse(
        val code: Int,
        val status: Int,
        val message: String,
        val data: List<Seat>,
        val success: Boolean
    )
    
    data class Seat(
        val code: Int,
        val name: String,
        val state: Int,
        val xpos: Int,
        val ypos: Int,
        val width: Int,
        val height: Int,
        val textSize: Int?,
        val seatTime: SeatTime?,
        val pcSeatYN: String
    )
    
    data class SeatTime(
        val idx: Long,
        val seatId: Int,
        val mySeat: Boolean,
        val confirmInTime: Long,
        val expireTime: Long
    )
}