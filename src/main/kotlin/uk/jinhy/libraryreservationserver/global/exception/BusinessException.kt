package uk.jinhy.libraryreservationserver.global.exception

import org.springframework.http.HttpStatus

class BusinessException(
    val errorCode: String,
    override val message: String,
    val status: HttpStatus,
) : RuntimeException(message) {
    companion object {
        fun badRequest(
            errorCode: String,
            message: String,
        ): BusinessException {
            return BusinessException(errorCode, message, HttpStatus.BAD_REQUEST)
        }

        fun unauthorized(
            errorCode: String,
            message: String,
        ): BusinessException {
            return BusinessException(errorCode, message, HttpStatus.UNAUTHORIZED)
        }

        fun forbidden(
            errorCode: String,
            message: String,
        ): BusinessException {
            return BusinessException(errorCode, message, HttpStatus.FORBIDDEN)
        }

        fun notFound(
            errorCode: String,
            message: String,
        ): BusinessException {
            return BusinessException(errorCode, message, HttpStatus.NOT_FOUND)
        }

        fun conflict(
            errorCode: String,
            message: String,
        ): BusinessException {
            return BusinessException(errorCode, message, HttpStatus.CONFLICT)
        }

        fun internalServerError(
            errorCode: String,
            message: String,
        ): BusinessException {
            return BusinessException(errorCode, message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
