package com.tenco.bank.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tenco.bank.repository.model.User;

@Mapper // MyBatis가 인식할 수 있게 됨
public interface UserRepository {
	
	public int insert(User user);
	
	public int updateById(User user);  
	
	public int deleteById(Integer id); // principal : 접근 주체
	
	// 전체 조회
	public List<User> findAll();
	
	// 특정 고객 조회
	public User findById(Integer id);
	
}
