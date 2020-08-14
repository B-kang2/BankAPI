package dev.kang.BankApp.data;

import java.util.Set;

import dev.kang.BankApp.beans.*;

public interface RoleDAO {
	public Integer createRole(Role r);//create
	public Role getRoleById(Integer id);//read
	public Set<Role> getRoles();//read
	public void updateRole(Role r); // Update
	public void deleteRole(Role r); // Delete
	

}
