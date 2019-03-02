package com.fombico.simplerestapi

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class CustomerNotFoundException : RuntimeException()