package dev.kang.BankApp.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import dev.kang.BankApp.beans.*;
import dev.kang.BankApp.data.*;
import dev.kang.BankApp.utils.ConnectionUtil;

public class AccountPostgres implements AccountDAO{
	private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();

	@Override
	public Integer createAccount(Account a) {
		Integer id = 0;

		try (Connection conn = cu.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "insert into Account values (default, ?, ?, ?)";
			String[] keys = {"id"};
			PreparedStatement pstmt = conn.prepareStatement(sql, keys);
			pstmt.setDouble(1, a.getBalance());
			pstmt.setInt(2, a.getStatus().getStatusId());
			pstmt.setInt(3, a.getType().getTypeId());
			
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			
			if (rs.next()) {
				id = rs.getInt("id");
				conn.commit();
			} else {
				conn.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return id;
	}


	//need to update here
	@Override
	public void updateAccount(Account a) {
		try (Connection conn = cu.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "update account set balance = ?, account_status_id = ?,"
					+ "account_type_id = ? where id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, a.getBalance());
			pstmt.setInt(2, a.getStatus().getStatusId());
			pstmt.setInt(3, a.getType().getTypeId());
			pstmt.setInt(4, a.getAccountId());
			
			//an excuteUpdate call can return the number
			//of rows affected by the statement
			int rowsAffected = pstmt.executeUpdate();
			//this should be 1 since we were updating 
			if (rowsAffected > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}

	@Override
	public void deleteAccount(Account a) {
		try (Connection conn = cu.getConnection()){
			conn.setAutoCommit(false);
			
			String sql = "delete from account where id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, a.getAccountId());
			
			int rowsAffected = pstmt.executeUpdate();
			
			if (rowsAffected > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public double getBalances(Account a)  {
		double balance = (Double) null;
		
		try (Connection conn = cu.getConnection()){
			String sql = "select A.id, A.balance, account_status_id, status.name as status_name, account_type_id, typ_name from" + 
					"(select account.id, account.balance, account.account_status_id, account.account_type_id, typ.name as typ_name from account" + 
					"join typ on account_type_id = typ.id) as A" + 
					"join status on account_status_id = status.id where id = ?";
			
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setInt(1, a.getAccountId());
			
			ResultSet rs = pstm.executeQuery();
			
			if (rs.next()) {
				balance = a.getBalance();
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return balance;
	}

	@Override
	public Account getAccountById(Integer id) {
		Account account = null;
		
		try (Connection conn = cu.getConnection()){
			String sql = "select A.id, A.balance, account_status_id, status.name as status_name, account_type_id, typ_name from" + 
					"	(select account.id, account.balance, account.account_status_id, account.account_type_id, typ.name as typ_name from account" + 
					"	join typ on account_type_id = typ.id) as A" + 
					"	join status on account_status_id = status.id where id = ?";
			
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setInt(1, id);
			
			ResultSet rs = pstm.executeQuery();
			
			if (rs.next()) {
				account = new Account();
				account.setAccountId(id);
				account.setBalance(rs.getDouble("balance"));
				
				AccountType t = new AccountType();
				t.setTypeId(rs.getInt("account_type_id"));
				t.setType(rs.getString("typ_name"));
				account.setType(t);
				
				
				AccountStatus s = new AccountStatus();
				s.setStatusId(rs.getInt("account_status_id"));
				s.setStatus(rs.getString("status_name"));
				account.setStatus(s);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return account;
	}



	@Override
	public Set<Account> getAccountsByStatus(AccountStatus s) {
		Set<Account> a = null;
		
		try(Connection conn = cu.getConnection()){
			String sql = "select A.id, A.balance, account_status_id, status.name as status_name, account_type_id, typ_name from" + 
					"	(select account.id, account.balance, account.account_status_id, account.account_type_id, typ.name as typ_name from account" + 
					"	join typ on account_type_id = typ.id) as A" + 
					"	join status on account_status_id = status.id where account_status_id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, s.getStatusId());
			
			ResultSet rs = pstmt.executeQuery();
			
			a = new HashSet <>();
			
			while (rs.next()) {
				Account acc = new Account ();
				acc.setAccountId(rs.getInt("id"));
				acc.setBalance(rs.getDouble("balance"));
				
				AccountType at = new AccountType();
				at.setTypeId(rs.getInt("account_type_id"));
				at.setType(rs.getString("typ_name"));
				
				acc.setType(at);
				
				acc.setStatus(s);				
				
				a.add(acc);
			}		
			
			
		} catch (Exception e){
			e.printStackTrace();
		}

		
		return a;
	}

	@Override
	public Set<Account> getAccountsByType(AccountType t) {
		Set<Account> a = null;
		
		try(Connection conn = cu.getConnection()){
			String sql = "select A.id, A.balance, account_status_id, status.name as status_name, account_type_id, typ_name from" + 
					"	(select account.id, account.balance, account.account_status_id, account.account_type_id, typ.name as typ_name from account" + 
					"	join typ on account_type_id = typ.id) as A" + 
					"	join status on account_status_id = status.id where account_type_id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, t.getTypeId());
			
			ResultSet rs = pstmt.executeQuery();
			
			a = new HashSet <>();
			
			while (rs.next()) {
				Account acc = new Account ();
				acc.setAccountId(rs.getInt("id"));
				acc.setBalance(rs.getDouble("balance"));
				
				AccountStatus as = new AccountStatus();
				as.setStatusId(rs.getInt("account_status_id"));
				as.setStatus(rs.getString("status_name"));
				
				acc.setStatus(as);
				
				acc.setType(t);				
				
				a.add(acc);
			}		
			
			
		} catch (Exception e){
			e.printStackTrace();
		}

		
		return a;
	}



	@Override
	public Set<Account> getAccounts() {
		Set<Account> accounts = null;
		
		try(Connection conn = cu.getConnection()){
			String sql = "select A.id, A.balance, account_status_id, status.name as status_name, account_type_id, typ_name from " + 
					"(select account.id, account.balance, account.account_status_id, account.account_type_id, typ.name as typ_name from account " + 
					"join typ on account_type_id = typ.id) as A" + 
					"join status on account_status_id = status.id;";
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			accounts = new HashSet<>();
			while(rs.next()) {
				Account a = new Account ();
				a.setAccountId(rs.getInt("id"));
				a.setBalance(rs.getDouble("balance"));
				
				AccountStatus as = new AccountStatus ();
				as.setStatusId(rs.getInt("account_status_id"));
				as.setStatus(rs.getString("status_name"));
				a.setStatus(as);
				
				AccountType at = new AccountType();
				at.setTypeId(rs.getInt("typ.id"));
				at.setType(rs.getString("typ_name"));
				a.setType(at);
				
				accounts.add(a);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return accounts;
	}


	@Override
	public void withdraw(Account a, double balance) {
		
		try(Connection conn = cu.getConnection()){
			String sql = "select * from account where id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, a.getAccountId());
			
			ResultSet rs = pstmt.executeQuery();

			
			if(rs.next()) {
				if (a.getBalance()>= balance) {
					a.setBalance(a.getBalance()-balance);
				} else {
					
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}


	@Override
	public void deposit(Account a, double balance) {
		try(Connection conn = cu.getConnection()){
			String sql = "select * from account where id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, a.getAccountId());
			
			ResultSet rs = pstmt.executeQuery();

			
			if(rs.next()) {
				a.setBalance(a.getBalance()+ balance);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	@Override
	public void transfer(Account a1, Account a2, double balance) {
		withdraw(a1, balance);
		deposit(a2, balance);
	}
	

}
