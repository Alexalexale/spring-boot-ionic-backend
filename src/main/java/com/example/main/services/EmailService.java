package com.example.main.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.example.main.domain.Cliente;
import com.example.main.domain.Pedido;

public interface EmailService {

	void senderOrderConfirmationEmail(Pedido pedido);

	void sendEmail(SimpleMailMessage mailMessage);

	void senderOrderConfirmationHtmlEmail(Pedido pedido);

	void sendHtmlEmail(MimeMessage mimeMessage);

	void sendNewPasswordEmail(Cliente cliente, String newPassword);
}