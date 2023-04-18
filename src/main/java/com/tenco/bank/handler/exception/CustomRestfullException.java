package com.tenco.bank.handler.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

// IoC 대상이 아님 (필요할 떄 직접 new 처리해서 사용)
@Getter
public class CustomRestfullException extends RuntimeException {

	private HttpStatus status;
	// throw new CustomRestfullException("페이지 못 찾음", 404);
	public CustomRestfullException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}
	
}
