package dev.kang.BankApp.data;

import java.util.Set;

import dev.kang.BankApp.beans.*;

public interface AccountDAO {
	public Integer createAccount(Account a); // Create
	public Set<Account> getAccountsByStatus(AccountStatus s); // Read
	public Set<Account> getAccountsByType(AccountType t);//Read
	public void updateAccount(Account a); // Update
	public void deleteAccount(Account a); // Delete
	public double getBalances(Account a);
	public Account getAccountById(Integer id);	
	public Set<Account> getAccounts();
	public void withdraw(Account a, double balance);
	public void deposit(Account a, double balance);
	public void transfer(Account a1, Account a2, double balance);

}
