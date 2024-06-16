package com.kambo.gymmy.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClient
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AmazonConfig(
        @Value("\${aws.s3.accessKey}") private val accessKey: String,
        @Value("\${aws.s3.secretKey}") private val secretKey: String,
) {
    @Bean
    fun s3Client(): AmazonS3  {
        val awsCredentials = BasicAWSCredentials(accessKey, secretKey)
        return AmazonS3ClientBuilder.standard()
                .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build()
    }

    @Bean
    fun cognitoIdentityProviderClient(): AWSCognitoIdentityProvider {
        val awsCredentials = BasicAWSCredentials(accessKey, secretKey)

        return AWSCognitoIdentityProviderClient.builder()
                .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build()
    }

}