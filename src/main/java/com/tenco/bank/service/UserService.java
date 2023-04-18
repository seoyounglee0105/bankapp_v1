package com.tenco.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bank.dto.SignInFormDto;
import com.tenco.bank.dto.SignUpFormDto;
import com.tenco.bank.handler.exception.CustomRestfullException;
import com.tenco.bank.repository.interfaces.UserRepository;
import com.tenco.bank.repository.model.User;

@Service // IoC 대상
public class UserService {

	// DAO 표준을 정의해둔 인터페이스
	@Autowired // 객체 생성 시 의존 주입 처리
	private UserRepository userRepository;
	
	// 트랜잭션 어노테이션
	@Transactional 
	// 메서드 호출이 시작될 때 트랜잭션의 시작
	// 메서드 종료 시 트랜잭션의 종료 (-> commit : 저장 장치에 실제로 저장)
	public void signUp(SignUpFormDto signUpFormDto) {
		// signUp의 매개변수는 DTO, insert의 매개변수는 Model
		
		int result = userRepository.insert(signUpFormDto);
		
		if (result != 1) {
			throw new CustomRestfullException("회원 가입에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR); // 연산 오류
		}
		
	}
	
	/**
	 * 
	 * @param signInFormDto
	 * @return userEntity
	 */
	
	public User signIn(SignInFormDto signInFormDto) {
		
		// 코딩 컨벤션
		// select 문을 통해 반환받은 model 객체의 이름은 model명 + Entity로
		User userEntity = userRepository.findByUsernameAndPassword(signInFormDto);
		
		if (userEntity == null) {
			throw new CustomRestfullException("아이디 또는 비밀번호가 틀렸습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return userEntity;
	}
	
}
