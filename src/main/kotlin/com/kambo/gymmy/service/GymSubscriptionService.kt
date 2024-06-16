package com.kambo.gymmy.service

import com.kambo.gymmy.api.rest.dto.request.CreateSubscriptionRequestDto
import com.kambo.gymmy.dao.entity.Subscription
import com.kambo.gymmy.dao.repository.SubscriptionRepository
import com.kambo.gymmy.exception.ResourceNotFoundException
import com.kambo.gymmy.service.*
import com.kambo.gymmy.service.`interface`.MediaService
import com.kambo.gymmy.service.`interface`.UserService
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
@Transactional
class GymSubscriptionService(
        private val subscriptionRepository: SubscriptionRepository,
        private val gymService: GymService,
        private val userService: UserService,
        private val mediaService: MediaService
) {

    fun createSubscription(gymId: Int, req: CreateSubscriptionRequestDto): Int {
        userService.isAllowedToModifyGymOrThrow(gymId)
        val gym = gymService.findByIdOrElseThrow(gymId)

        val subscription = Subscription(
                name = req.name,
                description = req.description,
                currency = req.currency,
                version = 1,
                price = req.price,
                createdById = userService.findCurrentUser().id,
                gym = gym,
                isActive = true,
                expireDate = req.expireDate
        )

        return subscriptionRepository.save(subscription).id!!
    }

    fun findAll(gymId: Int, pageable: Pageable): Page<Subscription> {
        return subscriptionRepository.findByGymId(gymId, pageable)
    }

    fun uploadMedia(gymId: Int, id: Int, file: MultipartFile): String {
        userService.isAllowedToModifyGymOrThrow(gymId)
        val subscription = subscriptionRepository.findById(id).orElseThrow { ResourceNotFoundException() }
        val uploadedMediaUrl = mediaService.uploadFile(file)

        val newImages = mutableListOf(uploadedMediaUrl).also {
            it.addAll(subscription.images)
        }.toList()

        subscriptionRepository.save(subscription.copy(images = newImages))

        return uploadedMediaUrl
    }
}