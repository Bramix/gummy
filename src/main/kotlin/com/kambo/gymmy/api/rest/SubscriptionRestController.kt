package com.kambo.gymmy.api.rest

import com.kambo.gymmy.api.rest.dto.request.CreateSubscriptionRequestDto
import com.kambo.gymmy.api.rest.dto.response.CreateSubscriptionResponseDto
import com.kambo.gymmy.api.rest.dto.response.FindAllSubscriptionsResponse
import com.kambo.gymmy.dao.entity.Subscription
import com.kambo.gymmy.service.GymSubscriptionService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/v1/{gymId}/subscriptions")
class SubscriptionRestController(
        private val subscriptionService: GymSubscriptionService,
) {
    @PostMapping
    fun create(@PathVariable gymId: Int, req: CreateSubscriptionRequestDto): CreateSubscriptionResponseDto =
            CreateSubscriptionResponseDto(subscriptionService.createSubscription(gymId, req))

    @GetMapping
    fun findAll(@PathVariable gymId: Int, pageable: Pageable): Page<FindAllSubscriptionsResponse> {
        val page: Page<Subscription> = subscriptionService.findAll(gymId, pageable)
        return PageImpl(page.content.map {
            it.toFindAllSubscriptionsResponseItem()
        }, page.pageable, page.totalElements)
    }

    @PostMapping("/{id}/medias", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadMedia(
            @PathVariable gymId: Int,
            @PathVariable id: Int,
            @RequestPart file: MultipartFile
    ): String = subscriptionService.uploadMedia(gymId, id, file)
}

fun Subscription.toFindAllSubscriptionsResponseItem() = FindAllSubscriptionsResponse(
        id!!,
        name,
        description,
        currency,
        price,
        isActive,
        createdById,
        images,
        expireDate,
        version
)