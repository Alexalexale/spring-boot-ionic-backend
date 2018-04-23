package com.example.main.resources.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.example.main.services.exceptions.AuthorizationException;
import com.example.main.services.exceptions.DataIntegrityException;
import com.example.main.services.exceptions.FileException;
import com.example.main.services.exceptions.MergeObjectException;
import com.example.main.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResoucerExceptionHandler {

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {

		StandardError standardError = new StandardError(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(),
				"Not Found", e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standardError);

	}

	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException e, HttpServletRequest request) {

		StandardError standardError = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
				"Integridade de dados.", e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);

	}

	@ExceptionHandler(MergeObjectException.class)
	public ResponseEntity<StandardError> mergeObject(MergeObjectException e, HttpServletRequest request) {

		StandardError standardError = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
				"Erro no merge dos objetos.", e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);

	}

	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<StandardError> authorization(AuthorizationException e, HttpServletRequest request) {

		StandardError standardError = new StandardError(System.currentTimeMillis(), HttpStatus.FORBIDDEN.value(),
				"Acesso negado;", e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(standardError);

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {

		ValidationError standardError = new ValidationError(System.currentTimeMillis(),
				HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação.", e.getMessage(), request.getRequestURI());

		e.getBindingResult().getFieldErrors()
				.forEach(fe -> standardError.addError(fe.getField(), fe.getDefaultMessage()));

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(standardError);

	}

	@ExceptionHandler(FileException.class)
	public ResponseEntity<StandardError> file(FileException e, HttpServletRequest request) {

		StandardError standardError = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
				"Erro de Arquivo;", e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);

	}

	@ExceptionHandler(AmazonServiceException.class)
	public ResponseEntity<StandardError> amazonService(AmazonServiceException e, HttpServletRequest request) {
		HttpStatus httpStatus = HttpStatus.valueOf(e.getErrorCode());
		StandardError standardError = new StandardError(System.currentTimeMillis(), httpStatus.value(),
				"Erro Amazon Service.", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(httpStatus).body(standardError);
	}

	@ExceptionHandler(AmazonClientException.class)
	public ResponseEntity<StandardError> amazonClient(AmazonClientException e, HttpServletRequest request) {

		StandardError standardError = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
				"Erro Amazon Client.", e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);

	}

	@ExceptionHandler(AmazonS3Exception.class)
	public ResponseEntity<StandardError> amazonS3(AmazonS3Exception e, HttpServletRequest request) {

		HttpStatus status = HttpStatus.valueOf(e.getErrorCode());

		StandardError standardError = new StandardError(System.currentTimeMillis(), status.value(), "Erro Amazon S3.",
				e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(status).body(standardError);

	}

}