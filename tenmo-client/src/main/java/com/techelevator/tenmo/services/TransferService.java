package com.techelevator.tenmo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

public class TransferService {
	
	public static String AUTH_TOKEN = "";
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser authenticatedUser;

    public TransferService(String url) {
        this.baseUrl = url;
    }
    
	
	  public User[] getUsers(){ 
		User[] listOfUsers = restTemplate.exchange(baseUrl + "users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody(); 
  		return listOfUsers;
	  }
	  
	  
	  public void createTransfer(Transfer transfer) {
		  restTemplate.exchange(baseUrl + "transfers", HttpMethod.POST, makeNewAuthEntity(transfer), Transfer.class);  
	  }
	  
	  public Transfer[] getTransfers(int userId) {
		 Transfer[] listOfTransfers = restTemplate.exchange(baseUrl + "transfers/" + userId, HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
		 return listOfTransfers;
	  }
	  
	  public String findByAccountId(int accountId) {
		  String usernameByAccountId = restTemplate.exchange(baseUrl + "users/" + accountId, HttpMethod.GET, makeAuthEntity(), String.class).getBody();
		  return usernameByAccountId;
	  }
	  
	  public Transfer[] getTransferById(int transferId) {
		  Transfer[] transferById = restTemplate.exchange(baseUrl + "accounts/transfers/" + transferId, HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
		  return transferById;
	  }
	  
	  public String getDescByTypeId(int transferTypeId) {
		  String descByTypeId = restTemplate.exchange(baseUrl + "transfers/type/" + transferTypeId, HttpMethod.GET, makeAuthEntity(), String.class).getBody();
		  return descByTypeId;
	  }
	  
	  public String getDescByStatusId(int transferStatusId) {
		  String descByStatusId = restTemplate.exchange(baseUrl + "transfers/status/" + transferStatusId, HttpMethod.GET, makeAuthEntity(), String.class).getBody();
		  return descByStatusId;
	  }
    
    
    private HttpEntity makeAuthEntity() {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setBearerAuth(AUTH_TOKEN);
	    HttpEntity entity = new HttpEntity<>(headers);
	    return entity;
	  }
    
    private HttpEntity<Transfer> makeNewAuthEntity(Transfer transfer) {
 	   HttpHeaders headers = new HttpHeaders();
 	   headers.setContentType(MediaType.APPLICATION_JSON);
 	   headers.setBearerAuth(AUTH_TOKEN);
 	   HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
 	   return entity;
    }
    

}
