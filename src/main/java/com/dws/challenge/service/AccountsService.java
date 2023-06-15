package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.AccountTransfer;
import com.dws.challenge.exception.NoOverDraftSupportException;
import com.dws.challenge.repository.AccountsRepository;
import lombok.Getter;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

	@Getter
	private final AccountsRepository accountsRepository;


	@Autowired
	public AccountsService(AccountsRepository accountsRepository) {
		this.accountsRepository = accountsRepository;
	}

	public void createAccount(Account account) {
		this.accountsRepository.createAccount(account);
	}

	public Account getAccount(String accountId) {
		return this.accountsRepository.getAccount(accountId);
	}

	public void transferFunds(AccountTransfer accountTransfer) {


		Account accountFromBal= getAccount(accountTransfer.getAccountFromId());
		Account accountToBal= getAccount(accountTransfer.getAccountToId());
		
		BigDecimal pendingAmt= accountFromBal.getBalance().subtract(accountTransfer.getAmountToTransfer());
		if(pendingAmt.compareTo(BigDecimal.ZERO) <0) {
			throw new NoOverDraftSupportException("Overdraft Not Supported");
		}
		
		synchronized (accountFromBal) {
			synchronized (accountToBal) {
				
				accountFromBal.setBalance(pendingAmt);
				accountsRepository.updateAccount(accountFromBal);

				accountToBal.setBalance(accountToBal.getBalance().add(accountTransfer.getAmountToTransfer()));
				accountsRepository.updateAccount(accountToBal);

			}

		}		

		SendEmail accountFromMail=new SendEmail();
		accountFromMail.setAccount(accountFromBal);
		accountFromMail.setMessage(accountTransfer.getAmountToTransfer()+" amount is debited from your account number:"+accountFromBal.getAccountId()+" .Your account balance is "+accountFromBal.getBalance());
		accountFromMail.start();

		SendEmail accountToMail=new SendEmail();
		accountToMail.setAccount(accountToBal);
		accountToMail.setMessage(accountTransfer.getAmountToTransfer()+" amount is credited to your account number:"+accountToBal.getAccountId()+" .Your account balance is "+accountToBal.getBalance());
		accountToMail.start();
	}
}
