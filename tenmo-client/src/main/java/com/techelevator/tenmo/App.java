package com.techelevator.tenmo;

import java.util.ArrayList;
import java.util.List;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.view.ConsoleService;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080/";

	private static final String MENU_OPTION_EXIT = "Exit";
	private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };

	private AuthenticatedUser currentUser;
	private ConsoleService console;
	private AuthenticationService authenticationService;
	private AccountService accountService;
	private TransferService transferService;

	public static void main(String[] args) {
		App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new AccountService(API_BASE_URL), new TransferService(API_BASE_URL));
		app.run();
	}

	public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService, TransferService transferService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.transferService = transferService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");

		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		try{
			double balance = accountService.getBalance(currentUser.getUser().getId());
			System.out.println("Your current account balance is: $" + String.format("%.2f", balance));
		}catch(NullPointerException npe){
			System.out.println("error");
		}

	}

	private void viewTransferHistory() {
		Transfer[] listOfTransfers =transferService.getTransfers(currentUser.getUser().getId());
		List<Transfer> newTransferList = new ArrayList<>();

		System.out.println("\n-------------------------------------------");
		System.out.println("Transfers\nID\t\t From/To\t\t Amount");
		System.out.println("-------------------------------------------\n");
		

		for (Transfer i : listOfTransfers) {
			newTransferList.add(i);
			if (i.getAccountTo() == accountService.getAccountIdByUserId(currentUser.getUser().getId())) {
			System.out.println(i.getTransferId() + "\t\tFrom: " +  transferService.findByAccountId(i.getAccountFrom()) + "\t\t$" + String.format("%.2f", i.getAmount()));
			} 
			
			if (i.getAccountFrom() == accountService.getAccountIdByUserId(currentUser.getUser().getId())) {				
			System.out.println(i.getTransferId() + "\t\tTo: " +  transferService.findByAccountId(i.getAccountTo()) + "\t\t$" + String.format("%.2f", i.getAmount()));
			}
		}
		
		int newTransferId = console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel)");
		
		boolean validTransferId = false;
		for(Transfer transfer : newTransferList) {
			if(transfer.getTransferId() == newTransferId) {
				validTransferId = true;
			}
		}while(!validTransferId) {
			System.out.println("Not a valid transfer ID");
			mainMenu();
		}
		
		Transfer[] transferDetails = transferService.getTransferById(newTransferId);
		for(Transfer i : transferDetails) {
		
		System.out.println("\n-------------------------------------------");
		System.out.println("Transfers Details");
		System.out.println("-------------------------------------------\n");
		System.out.println("Id: " + i.getTransferId());
		System.out.println("From: " + transferService.findByAccountId(i.getAccountFrom()));
		System.out.println("To: " + transferService.findByAccountId(i.getAccountTo()));
		System.out.println("Type: " + transferService.getDescByTypeId(i.getTransferTypeId()));
		System.out.println("Status: " + transferService.getDescByStatusId(i.getTransferStatusId()));
		System.out.println("Amount: $" + String.format("%.2f", i.getAmount()));
		}
	}


	private void viewPendingRequests() {
		System.out.println("No pending requests");
		mainMenu();

	}

	private void sendBucks() {
		User[] listOfUsers = transferService.getUsers();
		List<User> newList = new ArrayList<>();

		currentUser.getUser().getId();

		System.out.println("\n-------------------------------------------");
		System.out.println("Users\nID\t\t Name");
		System.out.println("-------------------------------------------\n");


		for(User i : listOfUsers) {
			if(!currentUser.getUser().getId().equals(i.getId())) {
				newList.add(i);
				System.out.println(i.getId() + "\t\t " + i.getUsername());
			}
		}

		int accountToId = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)");

		boolean validUserId = false;
		for(User user : newList) {
			if(user.getId().equals(accountToId)) {
				validUserId = true;
			}
		}
		while(!validUserId) {
			System.out.println("Not a valid ID");
			mainMenu();
		}

		String amountToSendAsString = console.getUserInput("Enter Amount to Send");
		double amountToSend = Double.parseDouble(amountToSendAsString);

		if(amountToSend > accountService.getBalance(currentUser.getUser().getId())){
			System.out.println("Not Enough Funds!");
			mainMenu();
		}
		if(amountToSend > 0) {
			Transfer transfer = new Transfer();
			transfer.setAmount(amountToSend);
			transfer.setAccountFrom(accountService.getAccountIdByUserId(currentUser.getUser().getId()));
			transfer.setAccountTo(accountService.getAccountIdByUserId(accountToId));
			transfer.setTransferStatusId(2);
			transfer.setTransferTypeId(2);

			transferService.createTransfer(transfer);
			accountService.addToBalance(accountToId, amountToSend);
			accountService.subtractFromBalance(currentUser.getUser().getId(), amountToSend);
			System.out.println("You have sent: $" + String.format("%.2f", amountToSend));
		
		}
	}



	private void requestBucks() {
		System.out.println("Not available at this time");
		mainMenu();
	}

	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
		while (!isRegistered) //will keep looping until user is registered
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				authenticationService.register(credentials);
				isRegistered = true;
				System.out.println("Registration successful. You can now login.");
			} catch(AuthenticationServiceException e) {
				System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
			}
		}
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}

	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
