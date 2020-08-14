package dev.kang.BankApp.services;

import java.util.Set;


import dev.kang.BankApp.beans.Account;
import dev.kang.BankApp.beans.AccountType;
import dev.kang.BankApp.beans.AccountStatus;
import dev.kang.BankApp.beans.User;
import dev.kang.BankApp.beans.Role;

import dev.kang.BankApp.data.UserDAO;
import dev.kang.BankApp.data.AccountDAO;
import dev.kang.BankApp.data.AccountStatusDAO;
import dev.kang.BankApp.data.AccountTypeDAO;
import dev.kang.BankApp.data.RoleDAO;

public class AccountService {
	private UserDAO userDao;
	private AccountDAO accountDao;
	private AccountStatusDAO accountStatusDao;
	private AccountTypeDAO accountTypeDao;
	private RoleDAO roleDao;

	public AccountService (UserDAO u, AccountDAO a, AccountStatusDAO as, AccountTypeDAO at) {
		userDao = u;
		accountDao = a;
		accountStatusDao = as;
		accountTypeDao = at;
	}
	
	public Set<Account> getAccounts() {
		return accountDao.getAccounts();
	}
	
	public Integer addAccount(Account a) {
		return accountDao.createAccount(a); 
	}
	
	public Account getAccountById(Integer id) {
		return accountDao.getAccountById(id);
	}
	
	public Set<Account> getAccountsByStatus(AccountStatus as) {
		return accountDao.getAccountsByStatus(as);
	}
	
	public void updateAccount(Account a) {
		accountDao.updateAccount(a);
	}
	
	public void deleteAccount(Account a) {
		accountDao.deleteAccount(a);
	}
	
	public Integer addType(AccountType at) {
		return accountTypeDao.createType(at);
	}
	
	public Set<AccountType> getTypes() {
		return accountTypeDao.getTypes();
	}
	
	public void withdrawBalance(Account a, double balance) {
		accountDao.withdraw(a, balance);
	}
	
	public void depositBalance(Account a, double balance) {
		accountDao.deposit(a, balance);
	}
	
	public void transferBalance (Account a1, Account a2, double balance) {
		accountDao.transfer(a1, a2, balance);
	}
	

	
}
