package com.tenco.bank.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
// JSON 스네이크를 카멜로 변환해서 받기
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class) 
public class OAuthToken {
	
	private String tokenType;
	private String accessToken;
	private Integer expiresIn;
	private String refreshToken;
	private Integer refreshTokenExpiresIn;
	
}
