package com.tenco.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bank.dto.DepositFormDto;
import com.tenco.bank.dto.SaveFormDto;
import com.tenco.bank.dto.TransferFormDto;
import com.tenco.bank.dto.WithdrawFormDto;
import com.tenco.bank.dto.response.HistoryDto;
import com.tenco.bank.handler.exception.CustomRestfullException;
import com.tenco.bank.repository.interfaces.AccountRepository;
import com.tenco.bank.repository.interfaces.HistoryRepository;
import com.tenco.bank.repository.model.Account;
import com.tenco.bank.repository.model.History;

@Service // IoC 대상
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private HistoryRepository historyRepository;

	/**
	 * 계좌 생성 기능
	 * @param saveFormDto
	 * @param principalId
	 */
	
	@Transactional
	public void createAccount(SaveFormDto saveFormDto, Integer principalId) {
		
		// SaveFormDto를 변경 or 신규 생성
		// 1단계
		Account account = new Account();
		account.setNumber(saveFormDto.getNumber());
		account.setPassword(saveFormDto.getPassword());
		account.setBalance(saveFormDto.getBalance());
		account.setUserId(principalId);
		int resultRowCount = accountRepository.insert(account);
		
		if (resultRowCount != 1) {
			throw new CustomRestfullException("계좌 생성 실패", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// 단일 계좌 검색 기능
	public Account readAccount(Integer id) {
		
		Account accountEntity = accountRepository.findById(id);
		
		if (accountEntity == null) {
			throw new CustomRestfullException("해당 계좌를 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return accountEntity;
	}
	
	// 계좌 목록 보기 기능
	@Transactional
	public List<Account> readAccountList(Integer userId) {
		
		List<Account> list = accountRepository.findByUserId(userId);
		
		return list;
	}

	@Transactional
	public void updateAccountWithdraw(WithdrawFormDto withdrawFormDto, Integer principleId) {
		
		Account accountEntity = accountRepository.findByNumber(withdrawFormDto.getWAccountNumber());
		
		// 계좌 존재 여부 확인
		if (accountEntity == null) {
			throw new CustomRestfullException("존재하지 않는 계좌입니다.", HttpStatus.BAD_REQUEST);
		}
		
		// 본인 계좌 여부 확인
		if (accountEntity.getUserId() != principleId) {
			throw new CustomRestfullException("본인 소유 계좌가 아닙니다.", HttpStatus.UNAUTHORIZED);
		}
		
		// 계좌 비밀번호 확인
		if (accountEntity.getPassword().equals(withdrawFormDto.getWAccountPassword()) == false) {
			throw new CustomRestfullException("출금 계좌 비밀번호가 틀렸습니다.", HttpStatus.UNAUTHORIZED);
		}
		
		// 잔액 확인
		if (accountEntity.getBalance() < withdrawFormDto.getAmount()) {
			throw new CustomRestfullException("계좌 잔액이 부족합니다.", HttpStatus.BAD_REQUEST);
		}
		
		// 출금 처리 
		// 모델 객체의 상태 값 변경 처리
		accountEntity.withdraw(withdrawFormDto.getAmount());
		// DB 갱신
		accountRepository.updateById(accountEntity);
		
		// 거래 내역 등록
		History history = new History();
		history.setAmount(withdrawFormDto.getAmount());
		history.setWBalance(accountEntity.getBalance());
		history.setDBalance(null);
		history.setWAccountId(accountEntity.getId());
		history.setDAccountId(null); // 기본값이 null이 아니므로 지정해주어야 함
		
		int resultRowCount = historyRepository.insert(history);
		if (resultRowCount != 1) {
			throw new CustomRestfullException("출금 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@Transactional
	public void updateAccountDeposit(DepositFormDto depositFormDto) {
		
		Account accountEntity = accountRepository.findByNumber(depositFormDto.getDAccountNumber());
		if (accountEntity == null) {
			throw new CustomRestfullException("해당 계좌가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// 객체 상태 값 변경
		accountEntity.deposit(depositFormDto.getAmount());
		accountRepository.updateById(accountEntity);
		
		History history = new History();
		history.setAmount(depositFormDto.getAmount());
		history.setWBalance(null);
		history.setDBalance(accountEntity.getBalance());
		history.setWAccountId(null);
		history.setDAccountId(accountEntity.getId());
		int resultRowCount = historyRepository.insert(history);
		if (resultRowCount != 1) {
			throw new CustomRestfullException("정상 처리되지 않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	// 이체 처리
	@Transactional
	public void updateAccountTransfer(TransferFormDto transferFormDto, Integer principalId) {
		// 출금 계좌 존재 여부 확인 (select)
		Account withdrawAccountEntity = accountRepository.findByNumber(transferFormDto.getWAccountNumber());
		if (withdrawAccountEntity == null) {
			throw new CustomRestfullException("출금 계좌가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// 입금 계좌 존재 여부 확인 (select)
		Account depositAccountEntity = accountRepository.findByNumber(transferFormDto.getDAccountNumber());
		if (depositAccountEntity == null) {
			throw new CustomRestfullException("입금 계좌가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// 출금 계좌의 본인 소유 여부 확인
		withdrawAccountEntity.checkOwner(principalId);
		
		// 출금 계좌 비밀번호 확인 
		withdrawAccountEntity.checkPassword(transferFormDto.getWAccountPassword());
		
		// 출금 계좌 잔액 여부 확인
		withdrawAccountEntity.checkBalance(transferFormDto.getAmount());
		
		// 출금 계좌 잔액 변경 (update)
		withdrawAccountEntity.withdraw(transferFormDto.getAmount());
		accountRepository.updateById(withdrawAccountEntity);
		
		// 입금 계좌 잔액 변경 (update)
		depositAccountEntity.deposit(transferFormDto.getAmount());
		accountRepository.updateById(depositAccountEntity);
		
		// 거래 내역 저장 (insert)
		History history = new History();
		history.setAmount(transferFormDto.getAmount());
		history.setWAccountId(withdrawAccountEntity.getId());
		history.setDAccountId(depositAccountEntity.getId());
		history.setWBalance(withdrawAccountEntity.getBalance());
		history.setDBalance(depositAccountEntity.getBalance());
		int resultRowCount = historyRepository.insert(history);
		
		if (resultRowCount != 1) {
			throw new CustomRestfullException("정상 처리되지 않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	/**
	 * 
	 * @param type = [all, deposit, withdraw] 
	 * @param id (account_id)
	 * @return 3가지 타입 거래 내역 - 입금, 출금, 입출금
	 */
	
	public List<HistoryDto> readHistoryListByAccount(String type, Integer id) {
		
		List<HistoryDto> historyDtoList = historyRepository.findByIdHistoryType(type, id);
		
		return historyDtoList;
	}
	
}
