package com.kambo.gymmy.api.rest.dto.response

import java.time.Instant

data class FindAllSubscriptionsResponse(
        val id: Int,
        val name: String,
        val description: String,
        val currency: String? = null,
        val price: Double? = null,
        val isActive: Boolean,
        val createdById: String,
        val images: List<String>,
        val expireDate: Instant,
        val version: Int,
)