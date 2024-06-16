package com.kambo.gymmy.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "The requested resource is not found")
class ResourceNotFoundException : RuntimeException() {
}