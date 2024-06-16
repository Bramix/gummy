package com.kambo.gymmy.api.rest.dto.request

data class UpdateGymRequestDto(
        val address: String,
        val location: String,
        val websiteLink: String?,
        val instagramLink: String?,
        val email: String,
        val phone: String,
)