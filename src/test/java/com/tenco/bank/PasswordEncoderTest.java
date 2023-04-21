package com.tenco.bank;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

	
	public static void main(String[] args) {
		// 기능 확인
		String password = "p1234";
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		
		System.out.println("기존 비밀번호 : " + password);
		System.out.println("암호화된 비밀번호 : " + hashedPassword);
		
		// 사용자 요청 값 : p1234
		// DB에 기록된 값 : $2a$10$fV8IBOltO1ALaFn5FlxpAOmDxlkU0YJ91O3UR3ZdGMqRFrudEqk3Os
		
		// 입력된 값과 DB에 기록된 암호화된 값이 동일한지 확인
		boolean isMatched = passwordEncoder.matches(password, hashedPassword);
		System.out.println("비밀번호 일치 여부 : " + isMatched);
		
	}
	
}
