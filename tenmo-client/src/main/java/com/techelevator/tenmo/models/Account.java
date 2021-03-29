package com.techelevator.tenmo.models;

public class Account {
	private int accountId;
	private int userId;
	private double balance;

	public Account(int accountId, int userId, double balance) {
		this.accountId = accountId;
		this.userId = userId;
		this.balance = balance;
	}

    @Override
    public String toString() {
        return "\n--------------------------------------------" +
                "\n Account Details" +
                "\n--------------------------------------------" +
                "\n User Id: " + userId +
                "\n Account Id:'" + accountId + '\'' +
                "\n Balance: " + balance;
    }
		

	public int getAccountId() {
		return accountId;
	}

	public int getUserId() {
		return userId;
	}

	public double getBalance() {
		return balance;
	}

	
}
