package com.tenco.bank.repository.model;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;

import com.tenco.bank.handler.exception.CustomRestfullException;
import com.tenco.bank.utils.DecimalUtil;

import lombok.Data;

/*
 * 모델 클래스
 * -> 값을 담아둘 수 있고, 기능도 추가해둘 수 있다.
 * -> DB에서 받아온 데이터는 모델 클래스라고 부르자.
 *
 * request와 response에서 주고받는 객체를 DTO라고 부르자.
 * -> 이제 구분하기
 *    (이전까지는 DTO + 모델클래스를 둘 다 DTO라고 불렀었음)
 * 
 * 필요하다면 기능(메서드)를 추가해둘 수 있다.
 */

@Data
public class Account {

	private Integer id;
	private String number;
	private String password;
	private Long balance;
	private Integer userId;
	private Timestamp createdAt;
	
	// 출금 처리
	public void withdraw(Long amount) {
		this.balance -= amount;
	}
	
	// 입금 처리
	public void deposit(Long amount) {
		this.balance += amount;
	}
	
	// 계좌 소유자 확인
	public void checkOwner(Integer principleId) {
		if (this.userId != principleId) {
			throw new CustomRestfullException("계좌 소유자가 아닙니다.", HttpStatus.FORBIDDEN); // 알아보기
		}
	}
	
	// 패스워드 체크
	public void checkPassword(String password) {
		if (!this.password.equals(password)) {
			throw new CustomRestfullException("계좌 비밀번호가 틀렸습니다.", HttpStatus.BAD_REQUEST);
		}
	}
	
	// 잔액 여부 확인 (출금 시)
	public void checkBalance(Long amount) {
		if (this.balance < amount) {
			throw new CustomRestfullException("출금 잔액이 부족합니다.", HttpStatus.BAD_REQUEST);
		}
	}
	
	public String formatBalance() {
		return DecimalUtil.decimalFormat(balance);
	}
	
}
