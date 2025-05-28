package uk.jinhy.libraryreservationserver.global.exception

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import uk.jinhy.libraryreservationserver.global.response.CommonResponse

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<CommonResponse<Nothing>> {
        return ResponseEntity
            .status(e.status)
            .body(CommonResponse.error(e.errorCode, e.message, e.status))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<CommonResponse<Nothing>> {
        val fieldError =
            e.bindingResult.fieldError ?: return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.error("INVALID_REQUEST", "유효하지 않은 요청입니다", HttpStatus.BAD_REQUEST))

        val message = "${fieldError.field}: ${fieldError.defaultMessage}"
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(CommonResponse.error("INVALID_REQUEST", message, HttpStatus.BAD_REQUEST))
    }

    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException): ResponseEntity<CommonResponse<Nothing>> {
        val fieldError =
            e.bindingResult.fieldError ?: return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.error("INVALID_REQUEST", "유효하지 않은 요청입니다", HttpStatus.BAD_REQUEST))

        val message = "${fieldError.field}: ${fieldError.defaultMessage}"
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(CommonResponse.error("INVALID_REQUEST", message, HttpStatus.BAD_REQUEST))
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(e: MissingServletRequestParameterException): ResponseEntity<CommonResponse<Nothing>> {
        val message = "필수 파라미터 ${e.parameterName}이(가) 누락되었습니다"
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(CommonResponse.error("MISSING_PARAMETER", message, HttpStatus.BAD_REQUEST))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<CommonResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(CommonResponse.error("INVALID_REQUEST_BODY", "요청 본문이 유효하지 않습니다", HttpStatus.BAD_REQUEST))
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException): ResponseEntity<CommonResponse<Nothing>> {
        val message = "${e.name} 파라미터의 타입이 올바르지 않습니다. 예상 타입: ${e.requiredType?.simpleName}"
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(CommonResponse.error("TYPE_MISMATCH", message, HttpStatus.BAD_REQUEST))
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(e: ConstraintViolationException): ResponseEntity<CommonResponse<Nothing>> {
        val message =
            e.constraintViolations.joinToString(", ") {
                "${it.propertyPath}: ${it.message}"
            }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(CommonResponse.error("CONSTRAINT_VIOLATION", message, HttpStatus.BAD_REQUEST))
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ResponseEntity<CommonResponse<Nothing>> {
        val message = "${e.method} 메소드가 지원되지 않습니다. 지원되는 메소드: ${e.supportedMethods?.joinToString(", ")}"
        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(CommonResponse.error("METHOD_NOT_ALLOWED", message, HttpStatus.METHOD_NOT_ALLOWED))
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun handleHttpMediaTypeNotSupportedException(e: HttpMediaTypeNotSupportedException): ResponseEntity<CommonResponse<Nothing>> {
        val message = "${e.contentType} 미디어 타입이 지원되지 않습니다. 지원되는 타입: ${e.supportedMediaTypes.joinToString(", ")}"
        return ResponseEntity
            .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
            .body(CommonResponse.error("UNSUPPORTED_MEDIA_TYPE", message, HttpStatus.UNSUPPORTED_MEDIA_TYPE))
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(e: NoHandlerFoundException): ResponseEntity<CommonResponse<Nothing>> {
        val message = "${e.httpMethod} ${e.requestURL} 경로를 찾을 수 없습니다"
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(CommonResponse.error("NOT_FOUND", message, HttpStatus.NOT_FOUND))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<CommonResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(CommonResponse.error("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR))
    }
}
