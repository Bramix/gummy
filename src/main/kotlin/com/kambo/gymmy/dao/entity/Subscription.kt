package com.kambo.gymmy.dao.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.Currency


@Entity
class Subscription(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,
        @Column(nullable = false)
        val name: String,
        @Column(nullable = false)
        val description: String,
        @Column(nullable = true)
        val currency: String? = null,
        @Column(nullable = true)
        val price: Double? = null,
        @Column(nullable = false)
        val isActive: Boolean,
        @Column(nullable = false)
        val version: Int,
        @Column(nullable = true)
        val createdById: String,
        @ElementCollection
        val images: List<String> = listOf(),
        val expireDate: Instant,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "gym_id")
        val gym: Gym
) {

    fun copy(
            name: String = this.name,
            description: String = this.description,
            currency: String? = this.currency,
            price: Double? = this.price,
            isActive: Boolean = this.isActive,
            version: Int = this.version,
            images: List<String> = this.images,
            expireDate: Instant = this.expireDate
    ): Subscription = Subscription(
            id = this.id,
            name = name,
            description = description,
            currency = currency,
            price = price,
            isActive = isActive,
            version = version,
            images = images,
            expireDate = expireDate,
            gym = this.gym,
            createdById = this.createdById
    )
}