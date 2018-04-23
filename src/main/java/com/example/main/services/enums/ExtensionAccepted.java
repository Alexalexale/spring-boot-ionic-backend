package com.example.main.services.enums;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.function.Function;

import com.example.main.services.exceptions.FileException;

public enum ExtensionAccepted {

	PNG("png", (imagemPng) -> {
		BufferedImage jpgImagem = new BufferedImage(imagemPng.getWidth(), imagemPng.getHeight(), BufferedImage.TYPE_INT_RGB);
		jpgImagem.createGraphics().drawImage(imagemPng, 0, 0, Color.WHITE, null);
		return jpgImagem;
	}), 
	JPG("jpg", (bf) -> bf);

	private String type;
	private Function<BufferedImage, BufferedImage> toJpg;

	private ExtensionAccepted(String type, Function<BufferedImage, BufferedImage> toJpg) {
		this.type = type;
		this.toJpg = toJpg;
	}

	public String getType() {
		return type;
	}

	public BufferedImage getJpg(BufferedImage bufferedImage) {
		return toJpg.apply(bufferedImage);
	}

	public static ExtensionAccepted getExtensionAccepted(String extension) {
		return Arrays.asList(ExtensionAccepted.values()).stream().filter(e -> e.getType().equals(extension)).findFirst()
				.orElseThrow(() -> new FileException("Formato inválido."));
	}

}
