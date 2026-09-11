package com.atm.model;

public class Account {
	private int accountNumber;
	private String holderName;
	private int pin;
	private double balance;
	private int wrongAttempt;
	private boolean accountLocked;
	
	public Account() {
	// defauult one
	}

	public Account(int accountNumber, String holderName, int pin, double balance, int wrongAttempt,
			boolean accountLocked) {
		this.accountNumber = accountNumber;
		this.holderName = holderName;
		this.pin = pin;
		this.balance = balance;
		this.wrongAttempt = wrongAttempt;
		this.accountLocked = accountLocked;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getHolderName() {
		return holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getWrongAttempt() {
		return wrongAttempt;
	}

	public void setWrongAttempt(int wrongAttempt) {
		this.wrongAttempt = wrongAttempt;
	}

	public boolean isAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}
	
	
	

}
