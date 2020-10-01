package com.github.oindiao.common.util;

import java.time.LocalDate;

import org.jeasy.random.EasyRandom;

public class EasyRandomWithEmail extends EasyRandom {

	/**
	 * 
	 */
	private static final long serialVersionUID = 247977396536220227L;

	public String email() {
		
		return new StringBuilder()
				.append(nextObject(String.class).substring(0,10))
				.append("@")
				.append(nextObject(String.class).substring(0,8))
				.append(".")
				.append(nextObject(String.class).substring(0,3))
		.toString().toLowerCase(); 
	}
	
	public String password() {
		return nextObject(String.class);
	}
}
