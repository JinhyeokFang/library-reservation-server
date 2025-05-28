package uk.jinhy.libraryreservationserver.global.response

import org.springframework.http.HttpStatus

data class CommonResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ErrorResponse? = null
) {
    companion object {
        fun <T> success(data: T): CommonResponse<T> {
            return CommonResponse(
                success = true,
                data = data
            )
        }

        fun error(errorCode: String, message: String, status: HttpStatus): CommonResponse<Nothing> {
            return CommonResponse(
                success = false,
                error = ErrorResponse(errorCode, message, status.value())
            )
        }
    }
}

data class ErrorResponse(
    val code: String,
    val message: String,
    val status: Int
)
