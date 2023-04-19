package com.tenco.bank.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bank.dto.SaveFormDto;
import com.tenco.bank.handler.exception.CustomPageException;
import com.tenco.bank.handler.exception.CustomRestfullException;
import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.model.Account;
import com.tenco.bank.repository.model.User;
import com.tenco.bank.service.AccountService;
import com.tenco.bank.utils.Define;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private HttpSession session;
	
	@Autowired
	private AccountService accountService;
	
	/**
	 * 계좌 목록 페이지
	 * @return 목록 페이지 이동
	 */
	
	@GetMapping({"/list", "/"}) // 다중 매핑
	public String list(Model model) { // 데이터를 내려주기 위해 Model을 매개변수에 선언

		// 인증 검사
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		
		if (principal == null) {
			throw new UnAuthorizedException("인증된 사용자가 아닙니다.", HttpStatus.UNAUTHORIZED); // 인증되지 않음 (401)
		}
				
		// View 화면으로 데이터를 내려 주는 기술
		// Model 또는 ModelAndView
		// Model을 더 권장함
		// ModelAndView는 동적으로 페이지를 반환할 때
		
		List<Account> accountList = accountService.readAccountList(principal.getId());
		
		// 담긴 값이 없다면
		if (accountList.isEmpty()) {
			model.addAttribute("accountList", null);
		// 담긴 값이 있다면
		} else {
			model.addAttribute("accountList", accountList);			
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
		
		// 인증 검사
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		
		if (principal == null) {
			throw new UnAuthorizedException("로그인 후 이용해주세요.", HttpStatus.UNAUTHORIZED); // 인증되지 않음 (401)
		}
		
		return "account/saveForm";
	}
	
	/**
	 * 계좌 생성 
	 * 인증 검사
	 * 유효성 검사 처리 - 0원 이상 입력 가능, 음수 입력 불가능
	 * @param saveFormDto
	 * @return 계좌 목록 페이지
	 */
	
	@PostMapping("/save-proc")
	public String saveProc(SaveFormDto saveFormDto) {
		
		// 인증 검사
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		
		if (principal == null) {
			throw new UnAuthorizedException("로그인 후 이용해주세요.", HttpStatus.UNAUTHORIZED); // 인증되지 않음 (401)
		}
		
		// 유효성 검사
		if (saveFormDto.getNumber() == null || saveFormDto.getNumber().isEmpty()) {
			throw new CustomRestfullException("계좌번호를 입력해주세요.", HttpStatus.BAD_REQUEST);
		}
		
		if (saveFormDto.getPassword() == null || saveFormDto.getPassword().isEmpty()) {
			throw new CustomRestfullException("비밀번호를 입력해주세요.", HttpStatus.BAD_REQUEST);
		}
		
		if (saveFormDto.getBalance() == null || saveFormDto.getBalance() < 0) {
			throw new CustomRestfullException("0 또는 0보다 큰 금액을 입력해주세요.", HttpStatus.BAD_REQUEST);
		}

		// 서비스 호출
		accountService.createAccount(saveFormDto, principal.getId());
		
		return "redirect:/account/list";
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
