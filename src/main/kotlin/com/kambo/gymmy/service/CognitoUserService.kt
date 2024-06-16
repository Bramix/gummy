package com.kambo.gymmy.service

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesRequest
import com.amazonaws.services.cognitoidp.model.AttributeType
import com.amazonaws.services.cognitoidp.model.ForbiddenException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kambo.gymmy.service.`interface`.UserInformation
import com.kambo.gymmy.service.`interface`.UserRole
import com.kambo.gymmy.service.`interface`.UserRoleInfo
import com.kambo.gymmy.service.`interface`.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service


@Service
@Primary
class CognitoUserService(
        @Value("\${aws.poolId}") private val poolId: String,
        private val cognitoClient: AWSCognitoIdentityProvider,
        private val objectMapper: ObjectMapper
) : UserService {

    companion object {
        private const val USER_ROLES_NAME = "custom:userRoles"
    }

    override fun findCurrentUser(): UserInformation {

        val claims: Map<*, *> = objectMapper.convertValue(SecurityContextHolder.getContext().authentication.principal, Map::class.java)
        val attributes = claims["attributes"] as Map<*, *>?
        val username = attributes!!["username"] as String
        val user = cognitoClient.adminGetUser(AdminGetUserRequest().apply {
            withUsername(username)
            withUserPoolId(poolId)
        })
        val firstName = user.getAttributeValue("given_name")
        val lastName = user.getAttributeValue("family_name")
        val email = user.getAttributeValue("email")
        val phone = user.getAttributeValue("phone")
        val dob = user.getAttributeValue("birthdate")

        val userRoles = user.getAttributeValue(USER_ROLES_NAME, default = "[]")

        val groupNames = objectMapper.readValue<List<UserRoleInfo>>(userRoles)

        return UserInformation(username, firstName, lastName, email, phone, dob, groupNames)
    }

    override fun enrichUserRoles(userRoles: List<UserRoleInfo>) {
        val currentUser = this.findCurrentUser()

        val allUserRoles = currentUser.roles.toMutableList().apply {
            this.addAll(userRoles)
        }

        val attributesToUpdate = listOf<AttributeType>(
                AttributeType().withName(USER_ROLES_NAME).withValue(objectMapper.writeValueAsString(allUserRoles)
                ))

        val updateUserAttributesRequest = AdminUpdateUserAttributesRequest().apply {
            withUserAttributes(attributesToUpdate)
            withUsername(currentUser.id)
            withUserPoolId(poolId)
        }

        cognitoClient.adminUpdateUserAttributes(updateUserAttributesRequest)

    }

    override fun isAllowedToModifyGymOrThrow(gymId: Int) {
        findCurrentUser().roles.find { role -> role.roles.any { it == UserRole.OWNER } && role.gymId == gymId }
                ?: throw ForbiddenException("The user is not allowed to modify the gym")
    }

    private fun AdminGetUserResult.getAttributeValue(name: String, default: String = ""): String =
            this.userAttributes.firstOrNull { it.name == name }?.value
                    ?: default


}
