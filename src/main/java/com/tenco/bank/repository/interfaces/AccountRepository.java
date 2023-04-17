package com.tenco.bank.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tenco.bank.repository.model.Account;

@Mapper
public interface AccountRepository {
	
	public int insert(Account account);
	
	public int updateById(Account account);
	
	public int deleteById(Integer id);
	
	// 특정 계좌 조회
	public Account findById(Integer id);
	
	// 모든 계좌 조회
	public List<Account> findAll();
	
}
