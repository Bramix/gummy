package com.kambo.gymmy.api.rest.dto.request

import java.time.Instant

data class CreateSubscriptionRequestDto(
        val name: String,
        val description: String,
        val currency: String,
        val price: Double? = null,
        val expireDate: Instant,
)
