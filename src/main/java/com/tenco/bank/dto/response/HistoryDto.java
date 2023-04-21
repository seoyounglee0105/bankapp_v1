package com.tenco.bank.dto.response;

import java.sql.Timestamp;
import java.text.DecimalFormat;

import com.tenco.bank.utils.DecimalUtil;
import com.tenco.bank.utils.TimestampUtil;

import lombok.Data;

@Data
public class HistoryDto {

	private Integer id;
	private Long amount;
	private Long balance;
	private String sender;
	private String receiver;
	private Timestamp createdAt;
	
	// 날짜 형식 변환
	public String formatCreatedAt() {
		return TimestampUtil.timestampToString(createdAt);
	}
	
	public String formatBalance() {
		return DecimalUtil.decimalFormat(balance);
	}
	
	public String formatAmount() {
		return DecimalUtil.decimalFormat(amount);
	}
	
}
