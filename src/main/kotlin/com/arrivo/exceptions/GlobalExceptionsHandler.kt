package com.arrivo.exceptions

import com.arrivo.utilities.Settings.Companion.ERROR_EMAIL_ALREADY_IN_USE_MESSAGE
import com.arrivo.utilities.Settings.Companion.ERROR_ID_NOT_FOUND_MESSAGE
import com.arrivo.utilities.Settings.Companion.ERROR_INVALID_VALUE_MESSAGE
import com.arrivo.utilities.Settings.Companion.ERROR_PHONE_NUMBER_EXISTS_MESSAGE
import com.arrivo.utilities.Settings.Companion.ERROR_UNEXPECTED_EXCEPTION_MESSAGE
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponseDTO> {
        val errors: List<String> = ex.bindingResult.fieldErrors.map { error ->
            "${error.field}: ${error.defaultMessage ?: ERROR_INVALID_VALUE_MESSAGE}"
        }

        val errorResponse = ErrorResponseDTO(
            code = HttpStatus.BAD_REQUEST.value(),
            errors = errors
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(IdNotFoundException::class)
    fun handleEmployeeNotFound(ex: IdNotFoundException): ResponseEntity<ErrorResponseDTO> {
        val errorResponse = ErrorResponseDTO(
            code = HttpStatus.NOT_FOUND.value(),
            errors = listOf(ex.message ?: ERROR_ID_NOT_FOUND_MESSAGE)
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponseDTO> {
        ex.printStackTrace()

        val errorResponse = ErrorResponseDTO(
            code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            errors = listOf(ex.message ?: ERROR_UNEXPECTED_EXCEPTION_MESSAGE)
        )

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolation(ex: DataIntegrityViolationException): ResponseEntity<ErrorResponseDTO> {
        val errorMessage = extractConstraintViolationDetails(ex)

        val errorResponse = ErrorResponseDTO(
            code = HttpStatus.CONFLICT.value(),
            errors = listOf(errorMessage)
        )

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse)
    }

    private fun extractConstraintViolationDetails(ex: DataIntegrityViolationException): String {
        val rootCauseMessage = ex.rootCause?.message ?: "Constraint violation"
        return when {
            rootCauseMessage.contains("phone_number") -> ERROR_PHONE_NUMBER_EXISTS_MESSAGE
            rootCauseMessage.contains("email") -> ERROR_EMAIL_ALREADY_IN_USE_MESSAGE
            else -> "Duplicate entry or constraint violation"
        }
    }
}
