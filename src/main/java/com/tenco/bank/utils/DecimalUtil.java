package com.tenco.bank.utils;

import java.text.DecimalFormat;

public class DecimalUtil {

	public static String decimalFormat(Long inputNumber) {
		
		// ex) 1,000,000 
		DecimalFormat df = new DecimalFormat("#,###");
		
		return df.format(inputNumber);
	}
	
	public static String decimalFormat(Integer inputNumber) {
		
		// ex) 1,000,000 
		DecimalFormat df = new DecimalFormat("#,###");
		
		return df.format(inputNumber);
	}
	
}
