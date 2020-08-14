package dev.kang.BankApp.data;

import java.util.Set;

import dev.kang.BankApp.beans.*;

public interface AccountStatusDAO {
	public Integer createStatus(AccountStatus as);//create
	public AccountStatus getStatusById(Integer id);//read
	public Set<AccountStatus> getStatus();//read
	public void updateStatus(AccountStatus as); // Update
	public void deleteStatus(AccountStatus as); // Delete

}
