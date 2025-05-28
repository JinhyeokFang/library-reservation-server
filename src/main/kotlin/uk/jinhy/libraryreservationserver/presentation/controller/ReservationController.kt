package uk.jinhy.libraryreservationserver.presentation.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import uk.jinhy.libraryreservationserver.global.response.CommonResponse
import uk.jinhy.libraryreservationserver.global.response.ErrorResponse
import uk.jinhy.libraryreservationserver.presentation.dto.ReservationListResponse

@Tag(name = "예약", description = "예약 관련 API")
interface ReservationController {
    @Operation(summary = "예약 목록 조회", description = "사용자의 모든 예약 목록을 조회합니다")
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "예약 목록 조회 성공", 
            content = [Content(
                mediaType = "application/json", 
                schema = Schema(implementation = CommonResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "500", 
            description = "서버 오류", 
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponse::class)
            )]
        )
    ])
    fun getReservationList(): ResponseEntity<CommonResponse<ReservationListResponse>>
}
