package com.atm.model;

public class MonthlyTransaction {
	 private String month;
	    private double deposit;
	    private double withdrawal;

	    public MonthlyTransaction() {
	    }

	    public MonthlyTransaction(String month,
	                              double deposit,
	                              double withdrawal) {

	        this.month = month;
	        this.deposit = deposit;
	        this.withdrawal = withdrawal;
	    }

	    public String getMonth() {
	        return month;
	    }

	    public void setMonth(String month) {
	        this.month = month;
	    }

	    public double getDeposit() {
	        return deposit;
	    }

	    public void setDeposit(double deposit) {
	        this.deposit = deposit;
	    }

	    public double getWithdrawal() {
	        return withdrawal;
	    }

	    public void setWithdrawal(double withdrawal) {
	        this.withdrawal = withdrawal;
	    }

}
