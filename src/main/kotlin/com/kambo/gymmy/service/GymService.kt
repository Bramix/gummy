package com.kambo.gymmy.service

import com.kambo.gymmy.api.rest.dto.request.CreateGymRequestDto
import com.kambo.gymmy.dao.entity.Gym
import com.kambo.gymmy.dao.repository.GymRepository
import com.kambo.gymmy.exception.ResourceNotFoundException
import com.kambo.gymmy.service.`interface`.MediaService
import com.kambo.gymmy.service.`interface`.UserRole
import com.kambo.gymmy.service.`interface`.UserRoleInfo
import com.kambo.gymmy.service.`interface`.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional
class GymService(
        private val gymRepository: GymRepository,
        private val mediaService: MediaService,
        private val userService: UserService
) {

    fun createGym(req: CreateGymRequestDto): Gym {
        val gym = gymRepository.save(Gym(
                req.address,
                req.websiteLink,
                req.instagramLink,
                req.location,
                req.email,
                req.phone,
        ))

        userService.enrichUserRoles(listOf(UserRoleInfo(gym.id!!, listOf(UserRole.OWNER))))

        return gym
    }

    fun update(
            id: Int,
            address: String,
            location: String,
            websiteLink: String?,
            instagramLink: String?,
            email: String,
            phone: String,
    ) {
        userService.isAllowedToModifyGymOrThrow(id)

        val existingGym = gymRepository.findById(id).orElseThrow()

        val updatedGym = existingGym.copy(
                address,
                location,
                websiteLink,
                instagramLink,
                email,
                phone
        )
        gymRepository.save(updatedGym)
    }

    fun uploadMedia(id: Int, file: MultipartFile): String {
        userService.isAllowedToModifyGymOrThrow(id)
        val gymInfo = gymRepository.findById(id).orElseThrow()
        val uploadedMediaUrl = mediaService.uploadFile(file)

        val newImages  = mutableListOf(uploadedMediaUrl).also {
            it.addAll(gymInfo.images)
        }.toList()

        gymRepository.save(gymInfo.copy(images = newImages))

        return uploadedMediaUrl
    }


    fun findByIdOrElseThrow(id: Int): Gym = gymRepository.findById(id).orElseThrow { ResourceNotFoundException() }
}