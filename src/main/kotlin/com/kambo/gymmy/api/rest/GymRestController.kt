package com.kambo.gymmy.api.rest

import com.kambo.gymmy.api.rest.dto.request.CreateGymRequestDto
import com.kambo.gymmy.api.rest.dto.request.UpdateGymRequestDto
import com.kambo.gymmy.api.rest.dto.response.CreateGymResponseDto
import com.kambo.gymmy.api.rest.dto.response.GetGymByIdResponseDto
import com.kambo.gymmy.api.rest.dto.response.toGetGymByIdResponseDto
import com.kambo.gymmy.service.GymService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/v1/gyms")
class GymRestController(
        private val gymService: GymService,
) {
    @PostMapping
    fun create(req: CreateGymRequestDto): CreateGymResponseDto =
            CreateGymResponseDto(gymService.createGym(req).id!!)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): GetGymByIdResponseDto =
            gymService.findByIdOrElseThrow(id).toGetGymByIdResponseDto()

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, req: UpdateGymRequestDto) =
            gymService.update(
                    id = id,
                    address = req.address,
                    location = req.location,
                    websiteLink = req.websiteLink,
                    instagramLink = req.instagramLink,
                    email = req.email,
                    phone = req.phone
            )

    @PostMapping("/{id}/medias", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadMedia(@PathVariable id: Int, @RequestPart file: MultipartFile): String = gymService.uploadMedia(id, file)
}