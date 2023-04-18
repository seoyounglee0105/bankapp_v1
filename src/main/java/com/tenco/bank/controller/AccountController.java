package com.tenco.bank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bank.handler.exception.CustomPageException;
import com.tenco.bank.handler.exception.CustomRestfullException;

@Controller
@RequestMapping("/account")
public class AccountController {

	/**
	 * 계좌 목록 페이지
	 * @return 목록 페이지 이동
	 */
	
	@GetMapping({"/list", "/"}) // 다중 매핑
	public void list() {
		
		// 예외 테스트 todo 삭제
//		throw new CustomRestfullException("인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED); // 401
		throw new CustomPageException("페이지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
		
//		return "account/list";
	}
	
	/**
	 * 출금 페이지
	 * @return
	 */
	
	@GetMapping("/withdraw")
	public String withdraw() {
		
		return "account/withdrawForm";
	}
	
	/**
	 * 입금 페이지
	 * @return
	 */
	
	@GetMapping("/deposit")
	public String deposit() {
		
		return "account/depositForm";
	}
	
	/**
	 * 이체 페이지
	 * @return
	 */
	
	@GetMapping("/transfer")
	public String transfer() {
		
		return "account/transferForm";
	}
	
	/**
	 * 계좌 생성 페이지
	 * @return
	 */
	
	@GetMapping("/save")
	public String save() {
		
		return "account/saveForm";
	}
	
	/**
	 * 계좌 상세 보기 페이지
	 * @return
	 */
	
	@GetMapping("/detail")
	public String detail() {
		
		return "";
	}
	
}
