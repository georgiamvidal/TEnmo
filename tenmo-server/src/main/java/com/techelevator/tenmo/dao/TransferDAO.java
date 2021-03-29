package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {

	List<Transfer> getTransfers(int userId);
	
	List<Transfer> getTransfersById(int transferId);
	
	void createTransfer (Transfer transfer);
	
	String getDescByTypeId (int transferTypeId);
	
	String getDescByStatusId (int transferStatusId);
}
