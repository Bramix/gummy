package com.kambo.gymmy.api.documentation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

@RestController
class RootController {

    @GetMapping("/")
    fun redirectToSwagger(): RedirectView {
        return RedirectView("/swagger-ui/index.html")
    }
}