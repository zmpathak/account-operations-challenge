package com.dws.challenge.repository;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.AccountNotExists;
import com.dws.challenge.exception.DuplicateAccountIdException;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void createAccount(Account account) throws DuplicateAccountIdException {
        Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
        if (previousAccount != null) {
            throw new DuplicateAccountIdException(
                    "Account id " + account.getAccountId() + " already exists!");
        }
        
    }

    @Override
    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    @Override
    public void clearAccounts() {
        accounts.clear();
    }
   
    @Override
    public void updateAccount(Account account)
    {
    	if(accounts.get(account.getAccountId()) != null) {
    		Account accountToUpdate=accounts.get(account.getAccountId());
    		accountToUpdate.setBalance(account.getBalance());
    	}
    	else {
    		throw new AccountNotExists("Account not exists with id "+account.getAccountId().toString());
    	}
        
    }

}
