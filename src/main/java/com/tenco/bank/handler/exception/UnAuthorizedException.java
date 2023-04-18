package com.tenco.bank.handler.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class UnAuthorizedException extends RuntimeException {

	private HttpStatus status;
	
	// 매개변수로 이동할 주소를 받으면 각 호출마다 다른 경로로 보낼 수 있게 설계도 가능
	public UnAuthorizedException(String message, HttpStatus status) {
		
		super(message);
		this.status = status;
		
		
	}
	
}
