package dev.kang.BankApp.data;

import java.util.List;
import java.util.Set;

import dev.kang.BankApp.beans.Account;
import dev.kang.BankApp.beans.AccountType;
import dev.kang.BankApp.beans.AccountStatus;
import dev.kang.BankApp.beans.User;
import dev.kang.BankApp.beans.Role;



public interface UserDAO {
	public Integer createUserId(User u); // Create
	public String getUsernameByEmail(String email); // Read
	public String getPasswordByUsername(String username);//Read
	public void updateUser(User u); // Update
	public void deleteUser(User u); // Delete
	public User getUserById(Integer id); //read
	public User getUserByUsernameAndPassword(String username, String passwd);
	public User getUserByUsername(String username);
	public Set<User> getUsersByRole(Role r);

}
