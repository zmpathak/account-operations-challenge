package com.dws.challenge.service;

import com.dws.challenge.domain.Account;

class SendEmail extends Thread{  

	private Account account;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void run(){  
		EmailNotificationService emailNotificationService=new EmailNotificationService();
		emailNotificationService.notifyAboutTransfer(account, message);
	}  
}
