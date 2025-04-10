package com.BankSystem.UserService.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.*

@ControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(
        UserAlreadyExistException::class
    )
    fun badRequestException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.BAD_REQUEST, e.message!!)
    }

    private fun generateErrorResponse(
        status: HttpStatus,
        message: String
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(Date(), status.value(), status.name ,message), status)
    }
}