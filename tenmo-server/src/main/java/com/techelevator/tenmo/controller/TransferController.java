package com.techelevator.tenmo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

@RestController
public class TransferController {
	
	private UserDAO userDAO;
	private TransferDAO transferDAO;
	
	public TransferController(TransferDAO transferDAO, UserDAO userDAO) {
		this.transferDAO = transferDAO;
		this.userDAO = userDAO;
	}
	
	@RequestMapping(path = "users", method = RequestMethod.GET)
	public List<User> getUser(){
		List<User> listOfUsers = new ArrayList<>();
		listOfUsers = userDAO.findAll();
		return listOfUsers;
	}
	
	@RequestMapping(path = "transfers/{userId}", method = RequestMethod.GET)
	public List<Transfer> getTransfers(@PathVariable int userId){
		List<Transfer> results = new ArrayList<>();
		results = transferDAO.getTransfers(userId);
		return results;
	}
	
	@RequestMapping(path = "accounts/transfers/{transferId}", method = RequestMethod.GET)
	public List<Transfer> getTransferById(@PathVariable int transferId) {
		List<Transfer> transfer = transferDAO.getTransfersById(transferId);
		return transfer;
	}
	
	@RequestMapping(path = "users/{accountId}", method = RequestMethod.GET)
	public String findByAccountId(@PathVariable int accountId) {
		String user = userDAO.findUsernameByAccountId(accountId);
		return user;
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "transfers", method = RequestMethod.POST)
	public void createTransfer(@RequestBody Transfer transfer) {
		transferDAO.createTransfer(transfer);
	}
	
	@RequestMapping(path = "transfers/type/{transferTypeId}", method = RequestMethod.GET)
	public String getDescByTypeId(@PathVariable int transferTypeId) {
		return transferDAO.getDescByTypeId(transferTypeId);
	}
	
	@RequestMapping(path = "transfers/status/{transferStatusId}", method = RequestMethod.GET)
	public String getDescByStatusId(@PathVariable int transferStatusId){
		return transferDAO.getDescByStatusId(transferStatusId); 
	}

}
