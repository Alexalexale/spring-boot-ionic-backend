package com.example.main.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.IOUtils;
import com.example.main.services.exceptions.FileException;

@Service
public class S3Service {

	@Autowired
	private AmazonS3 s3Client;

	@Value("${s3.bucket}")
	private String s3Bucket;

	public URI uploadFile(MultipartFile multipartFile) {
		try {
			String fileName = multipartFile.getOriginalFilename();
			InputStream inputStream = multipartFile.getInputStream();
			String type = multipartFile.getContentType();
			return uploadFile(inputStream, fileName, type);
		} catch (IOException e) {
			throw new FileException("Erro de io: ".concat(e.getMessage()), e);
		}
	}

	public URI uploadFile(InputStream inputStream, String fileName, String type) {
		try {
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType(type);
			byte[] byteArray = IOUtils.toByteArray(inputStream);
			objectMetadata.setContentLength(byteArray.length);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
			s3Client.putObject(s3Bucket, fileName, byteArrayInputStream, objectMetadata);
			return s3Client.getUrl(s3Bucket, fileName).toURI();
		} catch (URISyntaxException | IOException e) {
			throw new FileException("Erro ao converte URL.", e);
		}
	}

}