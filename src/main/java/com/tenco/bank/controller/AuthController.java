package com.tenco.bank.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.tenco.bank.dto.OAuthToken;

@Controller
public class AuthController {
	
	// yml에 선언한 tenco.key 가져옴 (초기 파라미터) 
	@Value("${tenco.key}")
	private String tencoKey;

	// 서버에서 서버로 통신
	@GetMapping("/auth/kakao/callback")
	@ResponseBody // view가 아니라 데이터 반환
	public String kakaoCallbackCode(@RequestParam String code) {
		
		System.out.println("tencoKey : " + tencoKey);
		System.out.println("카카오가 보낸 인가 코드 확인");
		System.out.println("code : " + code);
		
		// 서버 -> 서버
		RestTemplate restTemplate = new RestTemplate();
		
		// 헤더 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
		
		// 바디 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		// 문서를 보고
		params.add("grant_type", "authorization_code");
		params.add("client_id", "bd2e1dbb477ff324001caadfd0bc7f33");
		params.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");	
		params.add("code", code);
		
		HttpEntity<MultiValueMap<String, String>> kakaoReqEntity = new HttpEntity<>(params, headers);
		
		ResponseEntity<OAuthToken> responseToken 
			= restTemplate.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST,
									kakaoReqEntity, OAuthToken.class);
		
		System.out.println(responseToken.getBody().toString());
		
		// 액세스 토큰을 받음 -> 사용자의 정보를 가져올 수 있게 됨 (자원 요청)
		String userInfo = requestKakaoUserInfo(responseToken.getBody().getAccessToken());
		System.out.println(userInfo);
		
		// 우리 서버에 추가 작업해야 할 사항
		// 세션 처리
		// -> 선행 작업 : 회원 가입
		// user_tb -> username, password, email
		
		// select 확인 후 
		// 회원가입
		// 최초 소셜 접근 시, 카카오 닉네임으로 username 저장
		// 중복된 이름이 생길 수 있음 -> 임의 값을 붙여서 생성함
		// -> ex) 닉네임/4354
		// password (필수) <- 임의 값으로 DB에 저장

		
		// 로그인 처리 - 세션 발급
		
	
		
		return userInfo;
	}
	
	// 액세스 토큰을 매개변수로, 사용자 정보 요청
	private String requestKakaoUserInfo(String OAuthhToken) {
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		// Bearer 다음에 반드시 한 칸 띄어쓰기 (규칙)
		headers.add("Authorization", "Bearer " + OAuthhToken);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// 바디 생략 (get, post가 둘 다 되는 것을 봤을 때 바디가 없어도 될 것으로 추정)
		HttpEntity<String> profileReqEntity = new HttpEntity<>(headers);
		
		// https://kapi.kakao.com/v2/user/me
		ResponseEntity<String> response 
			= restTemplate.exchange("https://kapi.kakao.com/v2/user/me", 
									HttpMethod.GET, profileReqEntity, String.class);
		
		return response.getBody().toString();
	}
	
}
