package com.example.main.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.main.services.enums.ExtensionAccepted;
import com.example.main.services.exceptions.FileException;

@Service
public class ImageService {

	public BufferedImage getJpgImageFromFile(MultipartFile file) {
		try {
			String extension = FilenameUtils.getExtension(file.getOriginalFilename());
			return ExtensionAccepted.getExtensionAccepted(extension).getJpg(ImageIO.read(file.getInputStream()));
		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}

	public InputStream getInputStream(BufferedImage image, String extension) {
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageIO.write(image, extension, output);
			return new ByteArrayInputStream(output.toByteArray());
		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}

	public BufferedImage cropSquare(BufferedImage image) {
		int min = (image.getHeight() > image.getWidth()) ? image.getWidth() : image.getHeight();
		return Scalr.crop(image, (image.getWidth() / 2) - (min / 2), (image.getHeight() / 2) - (min / 2), min, min);
	}

	public BufferedImage resize(BufferedImage image, int size) {
		return Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, size);
	}
}