package com.kambo.gymmy.dao.entity

import jakarta.persistence.*
import java.time.Instant


@Entity
class Gym(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int? = null,
        @Column(nullable = false)
        val address: String,
        @Column(nullable = false)
        val location: String,
        @Column(nullable = true)
        val websiteLink: String? = null,
        @Column(nullable = true)
        val instagramLink: String? = null,
        @Column(nullable = false)
        val email: String,
        @Column(nullable = false)
        val phone: String,
        @Column(nullable = true)
        val payedDate: Instant? = null,
        @ElementCollection
        val images: List<String> = listOf(),
        val validated: Boolean = false,
        @OneToMany(mappedBy = "gym", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE], orphanRemoval = true)
        val subscriptions: MutableList<Subscription> = mutableListOf()
) {
    constructor(address: String, websiteLink: String?, instagramLink: String?, location: String, email: String, phone: String) : this(null, address, location, websiteLink, instagramLink, email, phone)

    fun copy(
            address: String = this.address,
            location: String = this.location,
            websiteLink: String? = this.websiteLink,
            instagramLink: String? = this.instagramLink,
            email: String = this.email,
            phone: String = this.phone,
            images: List<String> = this.images,
    ): Gym = Gym(
            id = this.id,
            address = address,
            location = location,
            websiteLink = websiteLink,
            instagramLink = instagramLink,
            email = email,
            phone = phone,
            payedDate = payedDate,
            images = images,
            validated = validated
    )
}