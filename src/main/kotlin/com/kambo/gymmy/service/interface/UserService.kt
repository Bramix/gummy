package com.kambo.gymmy.service.`interface`

interface UserService {
    fun findCurrentUser(): UserInformation
    fun enrichUserRoles(userRoles: List<UserRoleInfo>)
    fun isAllowedToModifyGymOrThrow(gymId: Int)
}

data class UserInformation(
        val id: String,
        val firstName: String,
        val lastName: String,
        val email: String,
        val phone: String,
        val dob: String,
        val roles: List<UserRoleInfo>
)

data class UserRoleInfo(
        val gymId: Int,
        val roles: List<UserRole>
)

enum class UserRole {
    COUCH, MODERATOR, OWNER
}
