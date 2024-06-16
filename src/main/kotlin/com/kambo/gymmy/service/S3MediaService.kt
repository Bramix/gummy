package com.kambo.gymmy.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.kambo.gymmy.service.`interface`.MediaService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*

@Service
class S3FileUploader(
        @Value("\${aws.s3.bucketName}") private val bucketName: String,
        private val s3Client: AmazonS3
) : MediaService {

    override fun uploadFile(multipartFile: MultipartFile): String {
        try {
            val objectMetadata = ObjectMetadata().apply {
                contentType = multipartFile.contentType
                contentLength = multipartFile.size
            }
            val fileName = UUID.randomUUID().toString()
            s3Client.putObject(bucketName, fileName, multipartFile.inputStream, objectMetadata)
            return "https://${bucketName}.s3.eu-central-1.amazonaws.com/${fileName}"
        } catch (e: IOException) {
            println("Error occurred ==> ${e.message}")
            throw FileUploadException("Error occurred in file upload ==> ${e.message}")
        }
    }
}

class FileUploadException(message: String) : RuntimeException(message)