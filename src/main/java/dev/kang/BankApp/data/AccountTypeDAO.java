package dev.kang.BankApp.data;

import java.util.Set;

import dev.kang.BankApp.beans.*;

public interface AccountTypeDAO {
	public Integer createType(AccountType at);//create
	public AccountType getTypeById(Integer id);//read
	public Set<AccountType> getTypes();//read
	public void updateType(AccountType at); // Update
	public void deleteType(AccountType at); // Delete

}
