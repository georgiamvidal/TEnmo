package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import com.techelevator.tenmo.model.Account;

@Component
public class JdbcAccountDAO implements AccountDAO {

	
	private JdbcTemplate jdbcTemplate;
	public JdbcAccountDAO(JdbcTemplate jdbcTemplate) {
	        this.jdbcTemplate = jdbcTemplate;
	    }

	@Override
	public double getBalance(int userId) {
		double balance = 0.0;
		try {
		String sql = "SELECT balance FROM accounts WHERE user_id = ?;";
		double results = jdbcTemplate.queryForObject(sql, Double.class, userId);
		return results;
	}catch(RestClientException ex) {
		System.out.println("Error");
	}return balance;
	
	}
	

	@Override
	public Double addToBalance(int userId, double amount) {
		String sql = "UPDATE accounts SET balance = (balance + ?) WHERE user_id = ?";
		double balance = jdbcTemplate.update(sql, amount, userId);
		return balance;
	}

	@Override
	public Double subtractFromBalance(int userId, double amount) {
		String sql = "UPDATE accounts SET balance = (balance - ?) WHERE user_id = ?";
		double balance = jdbcTemplate.update(sql, amount, userId);
		return balance;
	}

	@Override
	public int getAccountIdByUserId(int userId) {
		String sql = "SELECT account_id FROM accounts Where user_id = ?";
		
			int results = jdbcTemplate.queryForObject(sql, Integer.class, userId);
			return results;
	}

	@Override
	public int getUserIdByAccountId(int accountId) {
		String sql = "SELECT user_id FROM accounts WHERE account_id = ?";
		
			int results = jdbcTemplate.queryForObject(sql, Integer.class, accountId);
			return results;
			
	}
	

}
