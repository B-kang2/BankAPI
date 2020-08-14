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


public class UserService {
	private UserDAO userDao;
	private AccountDAO accountDao;
	private AccountStatusDAO accountStatusDao;
	private AccountTypeDAO accountTypeDao;
	private RoleDAO roleDao;
	
	public UserService (UserDAO u, RoleDAO r, AccountDAO a) {
		userDao = u;
		roleDao = r;
		accountDao = a;
	}
	
	public Integer registerUser (User u) {
		return userDao.createUserId(u);
	}
	
	public User logIn (String username, String password) {
		User u = userDao.getUserByUsernameAndPassword(username, password);
		if (password == u.getPassword()) {
			return u;
		} else {
			return null;
		}
		
	}
	
	public User findUserByName (String username) {		
		return userDao.getUserByUsername(username);
	}
	
	
	public User findUserById(Integer id) {
		return userDao.getUserById(id);
	}
	
	public void updatePassword(User u, String password) {
		u.setPassword(password);
		userDao.updateUser(u);
	}
	
	public void deleteUser(User u) {
		userDao.deleteUser(u);
	}
	
	public Set<Role> getRoles() {
		return roleDao.getRoles();
	}

}
