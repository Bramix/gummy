package com.kambo.gymmy.api.rest.dto.response

import com.kambo.gymmy.dao.entity.Gym

data class GetGymByIdResponseDto(
        val id: Int,
        val address: String,
        val location: String,
        val websiteLink: String?,
        val instagramLink: String?,
        val email: String,
        val phone: String,
        val images: List<String>
)

fun Gym.toGetGymByIdResponseDto() =
        GetGymByIdResponseDto(id!!, address, location, websiteLink, instagramLink, email, phone, images)
