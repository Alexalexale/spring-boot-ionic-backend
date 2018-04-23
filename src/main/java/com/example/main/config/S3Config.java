package com.example.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {

	@Value("${aws.access_key_id}")
	private String awsKeyId;

	@Value("${aws.secret_access_key}")
	private String awsSecretKey;

	@Value("${s3.region}")
	private String awsRegion;

	@Bean
	public AmazonS3 s3Client() {
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsKeyId, awsSecretKey);
		return AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(awsRegion))
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
	}

}
