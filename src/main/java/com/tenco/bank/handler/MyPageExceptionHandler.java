package com.tenco.bank.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.tenco.bank.handler.exception.CustomPageException;

/**
 * @ControllerAdvice
 * View 렌더링을 위해 ModelAndView 객체를 반환하도록 기본 설정되어 있음
 * 예외 page를 리턴하도록 활용할 예정
 */
@ControllerAdvice
public class MyPageExceptionHandler {

	// 사용자 정의 클래스 활용
	@ExceptionHandler(CustomPageException.class)
	public ModelAndView handleRuntimePageException(CustomPageException e) {
		// ModelAndView 활용 방법
		ModelAndView modelAndView = new ModelAndView("errorPage"); // 경로, jsp 생략 (yml에 명시)
		// ModelAndView에 값을 담아 보낼 수 있음
		modelAndView.addObject("statusCode", HttpStatus.NOT_FOUND.value()); // .value()하면 코드 숫자 반환 (404)
		modelAndView.addObject("message", e.getMessage());
		
		return modelAndView;
	}
	
	// 마이바티스 제약 오류 처리
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ModelAndView handleDataIntegrityViolationException(DataIntegrityViolationException e) {
		// ModelAndView 활용 방법
		ModelAndView modelAndView = new ModelAndView("errorPage"); // 경로, jsp 생략 (yml에 명시)
		// ModelAndView에 값을 담아 보낼 수 있음
		modelAndView.addObject("statusCode", HttpStatus.NOT_FOUND.value()); // .value()하면 코드 숫자 반환 (404)
		modelAndView.addObject("message", e.getMessage());
		
		return modelAndView;
	}
	
}
