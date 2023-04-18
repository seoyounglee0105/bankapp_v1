package com.tenco.bank.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bank.handler.exception.CustomPageException;
import com.tenco.bank.handler.exception.CustomRestfullException;
import com.tenco.bank.repository.model.User;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private HttpSession session;
	
	/**
	 * 계좌 목록 페이지
	 * @return 목록 페이지 이동
	 */
	
	@GetMapping({"/list", "/"}) // 다중 매핑
	public String list() {

		// 인증 검사
		User principal = (User) session.getAttribute("principal");
		
		if (principal == null) {
			throw new CustomRestfullException("인증된 사용자가 아닙니다.", HttpStatus.UNAUTHORIZED); // 인증되지 않음 (401)
		}
		
		return "account/list";
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
