package com.kambo.gymmy.config.secutity

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfiguration {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
                .sessionManagement {
                    it.sessionCreationPolicy(SessionCreationPolicy.NEVER)
                }

                .authorizeHttpRequests { authz ->
                    authz
                            .anyRequest().authenticated() // Secure all other endpoints
                }
                .oauth2Login(Customizer.withDefaults())
                .logout { logout ->
                    logout.logoutSuccessUrl("/")
                }
        return http.build()
    }
}