package com.tenco.bank.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tenco.bank.dto.DepositFormDto;
import com.tenco.bank.dto.SaveFormDto;
import com.tenco.bank.dto.TransferFormDto;
import com.tenco.bank.dto.WithdrawFormDto;
import com.tenco.bank.dto.response.HistoryDto;
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
				
		// View 화면으로 데이터를 내려 주는 기술
		// Model 또는 ModelAndView
		// Model을 더 권장함
		// ModelAndView는 동적으로 페이지를 반환할 때
		
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		
		List<Account> accountList = accountService.readAccountList(principal.getId());
		
		// 담긴 값이 없다면
		if (accountList.isEmpty()) {
			model.addAttribute("accountList", null); // 사실 지금 상황에서는 null 처리해줄 필요 없음 
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
	
	// 출금 처리
	
	@PostMapping("/withdraw-proc")
	public String withdrawProc(WithdrawFormDto withdrawFormDto) {
		
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		
		// 유효성 검사
		if (withdrawFormDto.getAmount() == null) {
			throw new CustomRestfullException("출금액을 입력하세요.", HttpStatus.BAD_REQUEST);
		} else if (withdrawFormDto.getAmount().longValue() <= 0) { // Long 타입의 값은 .longValue()로 가져옴
			throw new CustomRestfullException("출금액이 0원 이하일 수는 없습니다.", HttpStatus.BAD_REQUEST);
		}
		
		if (withdrawFormDto.getWAccountNumber() == null || withdrawFormDto.getWAccountNumber().isEmpty()) {
			throw new CustomRestfullException("계좌번호를 입력하세요.", HttpStatus.BAD_REQUEST);			
		}
		
		if (withdrawFormDto.getWAccountPassword() == null || withdrawFormDto.getWAccountPassword().isEmpty()) {
			throw new CustomRestfullException("비밀번호를 입력하세요.", HttpStatus.BAD_REQUEST);			
		}
		
		// 서비스 호출
		accountService.updateAccountWithdraw(withdrawFormDto, principal.getId());
		
		return "redirect:/account/list"; // redirect는 새로운 request, response를 생성함
	}
	
	/**
	 * 입금 페이지
	 * @return
	 */
	
	@GetMapping("/deposit")
	public String deposit() {
		
		return "account/depositForm";
	}
	
	@PostMapping("/deposit-proc")
	public String depositProc(DepositFormDto depositFormDto) {
		
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		
		if (depositFormDto.getAmount() == null) {
			throw new CustomRestfullException("금액을 입력해주세요.", HttpStatus.BAD_REQUEST);
		}
		
		if (depositFormDto.getAmount().longValue() <= 0) {
			throw new CustomRestfullException("입금 금액이 0원 이하일 수 없습니다.", HttpStatus.BAD_REQUEST);
		}
		
		if (depositFormDto.getDAccountNumber() == null || depositFormDto.getDAccountNumber().isEmpty()) {
			throw new CustomRestfullException("계좌번호를 입력하세요.", HttpStatus.BAD_REQUEST);
		}
		
		// 서비스 호출
		accountService.updateAccountDeposit(depositFormDto);
		
		return "redirect:/account/list";
	}
	
	/**
	 * 이체 페이지
	 * @return
	 */
	
	@GetMapping("/transfer")
	public String transfer() {
		
		return "account/transferForm";
	}
	
	// 이체 기능
	@PostMapping("/transfer-proc")
	public String transferProc(TransferFormDto transferFormDto) {
		
		// 인증 검사
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		
		// 유효성 검사
		// 출금 계좌번호 입력 여부
		if (transferFormDto.getWAccountNumber() == null || transferFormDto.getWAccountNumber().isEmpty()) {
			throw new CustomRestfullException("출금 계좌번호를 입력하세요.", HttpStatus.BAD_REQUEST);
		}

		// 입력 계좌번호 입력 여부
		if (transferFormDto.getDAccountNumber() == null || transferFormDto.getDAccountNumber().isEmpty()) {
			throw new CustomRestfullException("입금 계좌번호를 입력하세요.", HttpStatus.BAD_REQUEST);
		}
		
		// 출금 계좌 비밀번호 입력 여부
		if (transferFormDto.getWAccountPassword() == null || transferFormDto.getWAccountPassword().isEmpty()) {
			throw new CustomRestfullException("출금 계좌 비밀번호를 입력하세요.", HttpStatus.BAD_REQUEST);
		}
		
		// 이체 금액 확인
		if (transferFormDto.getAmount() == null) {
			throw new CustomRestfullException("이체 금액을 입력하세요.", HttpStatus.BAD_REQUEST);
		}
		
		if (transferFormDto.getAmount() <= 0) {
			throw new CustomRestfullException("이체 금액을 1원 이상 입력하세요.", HttpStatus.BAD_REQUEST);
		}
		
		// 출금 계좌번호와 입금 계좌번호가 동일하지 않은지 확인
		if (transferFormDto.getWAccountNumber().equals(transferFormDto.getDAccountNumber())) {
			throw new CustomRestfullException("출금 계좌번호와 입금 계좌번호가 동일합니다.", HttpStatus.BAD_REQUEST);
		}
		
		// 서비스 호출
		accountService.updateAccountTransfer(transferFormDto, principal.getId());
		
		return "redirect:/account/list";
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
	
	@GetMapping("/detail/{id}")                                     
	public String detail(@PathVariable Integer id, @RequestParam(required = false, name = "type", defaultValue = "all") String type,  Model model) {
		
		// 인증 검사
		User principal = (User) session.getAttribute(Define.PRINCIPAL);

		// 화면을 구성하기 위해 필요한 데이터
		
		// 소유자 정보 (세션에서 가져옴)
		model.addAttribute(Define.PRINCIPAL, principal);
		
		// 계좌 번호 (1개), 계좌 잔액
		Account account = accountService.readAccount(id);
		model.addAttribute("account", account);
		
		// 거래 내역
		List<HistoryDto> historyDtoList = accountService.readHistoryListByAccount(type, id);
		model.addAttribute("historyList", historyDtoList);
		
		return "/account/detail";
	}
	
}
