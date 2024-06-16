package com.kambo.gymmy.dao.repository

import com.kambo.gymmy.dao.entity.Gym
import org.springframework.data.jpa.repository.JpaRepository

interface GymRepository : JpaRepository<Gym, Int> {
}