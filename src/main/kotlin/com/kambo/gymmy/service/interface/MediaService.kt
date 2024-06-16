package com.kambo.gymmy.service.`interface`

import org.springframework.web.multipart.MultipartFile

interface MediaService {

    fun uploadFile(multipartFile: MultipartFile): String

}