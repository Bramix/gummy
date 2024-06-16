package com.kambo.gymmy.dao.repository

import com.kambo.gymmy.dao.entity.Subscription
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SubscriptionRepository : JpaRepository<Subscription, Int> {
    @Query("SELECT s FROM Subscription s WHERE s.gym.id = :gymId")
    fun findByGymId(gymId: Int, pageable: Pageable): Page<Subscription>
}