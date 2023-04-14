package com.tenco.bank.repository.model;

import java.sql.Timestamp;

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
	
	public void withdraw(Long amount) {
		this.balance -= amount;
	}
	
	public void deposit(Long amount) {
		this.balance += amount;
	}
	
	// 패스워드 체크
	// 잔액 여부 확인 (출금 시)
	// 계좌 소유자 확인
	
}
