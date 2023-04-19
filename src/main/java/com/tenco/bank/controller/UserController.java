package com.tenco.bank.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bank.dto.SignInFormDto;
import com.tenco.bank.dto.SignUpFormDto;
import com.tenco.bank.handler.exception.CustomRestfullException;
import com.tenco.bank.repository.model.User;
import com.tenco.bank.service.UserService;
import com.tenco.bank.utils.Define;

@Controller
@RequestMapping("/user")
public class UserController {
	
	// 서비스
	@Autowired // 객체 생성 시 의존 주입 처리
	private UserService userService;
	
	@Autowired // 해주면 예전처럼 request.getSession하지 않아도 가져와짐 
	private HttpSession session;

	// http://localhost:8080/user/sign-up
	@GetMapping("/sign-up")
	public String signUp() {
		
		return "user/signUp";
	}
	
	/**
	 * 회원 가입 처리
	 * @param signUpFormDto
	 * @return redirect 로그인 페이지
	 */
	
	@PostMapping("/sign-up")  // Post 방식에서 @RequestBody 없으면 Form 태그에서 땡겨온다는 뜻
	public String signUpProc(SignUpFormDto signUpFormDto) {
		
		// 1. 유효성 검사 (프론트에서 한 번, 백에서 한 번 해주는 게 바람직)
		if (signUpFormDto.getUsername() == null || signUpFormDto.getUsername().isEmpty()) {
			throw new CustomRestfullException("username을 입력해주세요.", HttpStatus.BAD_REQUEST);
		}
		
		if (signUpFormDto.getPassword() == null || signUpFormDto.getPassword().isEmpty()) {
			throw new CustomRestfullException("password를 입력해주세요.", HttpStatus.BAD_REQUEST);
		}
		
		if (signUpFormDto.getFullname() == null || signUpFormDto.getFullname().isEmpty()) {
			throw new CustomRestfullException("fullname을 입력해주세요.", HttpStatus.BAD_REQUEST);
		}
		
		//------- 유효성 검사 통과 시
		
		// 2. 서비스 호출 (핵심 로직은 서비스에서)
		userService.createUser(signUpFormDto);
		
		// 로그인 페이지로 보냄
		return "redirect:/user/sign-in";
	}
	
	/**
	 * 로그인 폼
	 * @return 로그인 페이지
	 */
	
	@GetMapping("/sign-in")
	public String signIn() {
		
		return "user/signIn";
	}
	
	/**
	 * 로그인 처리
	 * @param signInFormDto
	 * @return 메인 페이지 (수정 예정)
	 * 
	 * GET 방식 처리는 브라우저 히스토리에 남겨짐
	 * => 예외적으로, 로그인은 POST 방식으로 처리함 (보안 상의 이유)
	 */
	
	@PostMapping("/sign-in")
	public String signInProc(SignInFormDto signInFormDto) {
		
		// 1. 유효성 검사 (인증 필요하다면 인증 검사 먼저 - 여기는 ㄴㄴ)
		if (signInFormDto.getUsername() == null || signInFormDto.getUsername().isEmpty()) {
			throw new CustomRestfullException("username을 입력하세요.", HttpStatus.BAD_REQUEST);
		}
		
		if (signInFormDto.getPassword() == null || signInFormDto.getPassword().isEmpty()) {
			throw new CustomRestfullException("password를 입력하세요.", HttpStatus.BAD_REQUEST);
		}
		
		// 2. 서비스 호출
		// principal : 접근 주체 (코딩 컨벤션 : 세션에 저장할 정보 변수의 이름을 이렇게 사용함)
		// session은 어차피 서버에서만 접근할 수 있고 클라이언트는 접근할 수 없기 때문에
		// 비밀번호 포함해도 상관 없음 그래도 신경써주는 습관이 좋다
		
		User principal = userService.signIn(signInFormDto);
		principal.setPassword(null);
		
		// 3. 사용자 정보를 세션에 저장
		session.setAttribute(Define.PRINCIPAL, principal);
		
		// todo 변경 예정
		return "/account/list";
	}
	
	@GetMapping("/logout")
	public String logout() {
		session.invalidate(); // 로그아웃 처리
		
		return "redirect:/user/sign-in";
	}
	
}
