package com.example.main.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.main.domain.Cliente;
import com.example.main.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	@Value("${default.sender}")
	private String sender;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public void senderOrderConfirmationEmail(Pedido pedido) {
		SimpleMailMessage mailMessage = prepareSimpleMailMessage(pedido);
		sendEmail(mailMessage);
	}

	@Override
	public void senderOrderConfirmationHtmlEmail(Pedido pedido) {
		try {
			MimeMessage mimeMessage = prepareMimeMessage(pedido);
			sendHtmlEmail(mimeMessage);
		} catch (MessagingException e) {
			senderOrderConfirmationEmail(pedido);
		}
	}

	@Override
	public void sendNewPasswordEmail(Cliente cliente, String newPassword) {
		SimpleMailMessage mailMessage = prepareNewPasswordEmail(cliente, newPassword);
		sendEmail(mailMessage);
	}

	private SimpleMailMessage prepareNewPasswordEmail(Cliente cliente, String newPassword) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(cliente.getEmail());
		simpleMailMessage.setFrom(sender);
		simpleMailMessage.setSubject("Solicitação da nova senha");
		simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
		simpleMailMessage.setText("NOva senha: ".concat(newPassword));
		return simpleMailMessage;
	}

	protected MimeMessage prepareMimeMessage(Pedido pedido) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		mimeMessageHelper.setTo(pedido.getCliente().getEmail());
		mimeMessageHelper.setFrom(sender);
		mimeMessageHelper.setSubject("Pedido confirmado! Código: ".concat(pedido.getId().toString()));
		mimeMessageHelper.setSentDate(new Date(System.currentTimeMillis()));
		mimeMessageHelper.setText(htmlFromTemplatePedido(pedido), true);
		return mimeMessage;
	}

	protected SimpleMailMessage prepareSimpleMailMessage(Pedido pedido) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(pedido.getCliente().getEmail());
		simpleMailMessage.setFrom(sender);
		simpleMailMessage.setSubject("Pedido confirmado! Código: ".concat(pedido.getId().toString()));
		simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
		simpleMailMessage.setText(pedido.toString());
		return simpleMailMessage;
	}

	protected String htmlFromTemplatePedido(Pedido pedido) {
		Context context = new Context();
		context.setVariable("pedido", pedido);
		return templateEngine.process("email/confirmacaoPedido", context);
	}

}