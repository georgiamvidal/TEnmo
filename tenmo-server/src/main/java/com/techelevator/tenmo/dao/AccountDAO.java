package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {
	
	double getBalance(int userId);
	
	Double subtractFromBalance(int userId, double amount);
	
	int getAccountIdByUserId(int userId);
	
	int getUserIdByAccountId(int accountId);
	
	Double addToBalance(int userId, double amount);
	

}
