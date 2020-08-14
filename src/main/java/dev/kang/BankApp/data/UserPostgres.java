package dev.kang.BankApp.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dev.kang.BankApp.beans.*;
import dev.kang.BankApp.data.*;
import dev.kang.BankApp.utils.ConnectionUtil;

public class UserPostgres implements UserDAO{
	private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();
	private AccountDAO accountDao = new AccountPostgres();

	@Override
	public Integer createUserId(User u) {
		Integer id = 0;
		
		try (Connection conn = cu.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "insert into usr values (default, ?, ?, ?, ?, ?, ?)";
			String[] keys = {"id"};
			PreparedStatement pstmt = conn.prepareStatement(sql, keys);
			pstmt.setString(1, u.getUsername());
			pstmt.setString(2, u.getPassword());
			pstmt.setString(3, u.getFirstName());
			pstmt.setString(4, u.getLastName());
			pstmt.setString(5, u.getEmail());
			pstmt.setInt(6, u.getRole().getRoleId());
			
			
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

	@Override
	public String getUsernameByEmail(String email) {
		User u = new User();
		
		try (Connection conn = cu.getConnection()) {
			String sql = "select usr.id, usr.username, usr.passwd, usr.firstname, usr.lastname, usr.email, usr.user_role_id, user_role.name" + 
					"as role_name from usr" + 
					"join user_role on user_role_id = user_role.id where email = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				u.setEmail(rs.getString("username"));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return u.getUsername();
	}

	@Override
	public User getUserByUsernameAndPassword(String username, String passwd) {
		User u = new User();
		
		try (Connection conn = cu.getConnection()) {
			String sql = "select usr.id, usr.username, usr.passwd, usr.firstname, usr.lastname, usr.email, usr.user_role_id, user_role.name" + 
					"as role_name from usr" + 
					"join user_role on user_role_id = user_role.id where username = ? and passwd = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, passwd);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				u.setEmail(rs.getString("username"));
				u.setUserId(rs.getInt("id"));
				u.setFirstName(rs.getString("firstname"));
				u.setLastName(rs.getString("lastname"));
				u.setEmail(rs.getString("email"));
				u.setPassword(rs.getString("passwd"));
				
				Role r = new Role ();
				r.setRoleId(rs.getInt("user_role_id"));
				r.setRole(rs.getString("role_name"));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return u;
	}

	@Override
	public String getPasswordByUsername(String username) {
		User u = new User();
		
		try (Connection conn = cu.getConnection()) {
			String sql = "select usr.id, usr.username, usr.passwd, usr.firstname, usr.lastname, usr.email, usr.user_role_id, user_role.name" + 
					"as role_name from usr" + 
					"join user_role on user_role_id = user_role.id where username = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				u.setPassword(rs.getString("passwd"));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return u.getPassword();
	}

	@Override
	public Set<User> getUsersByRole(Role r) {
		Set <User> users = new HashSet<>();
		
		try (Connection conn = cu.getConnection()){
			String sql = "select usr.id, usr.username, usr.passwd, usr.firstname, usr.lastname, usr.email, usr.user_role_id, user_role.name" + 
					"as role_name from usr\r\n" + 
					"join user_role on user_role_id = user_role.id where user_role_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, r.getRoleId());
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				User u = new User ();
				u.setUserId(rs.getInt("id"));
				u.setUsername(rs.getString("username"));
				u.setPassword(rs.getString("passwd"));
				u.setEmail(rs.getString("email"));
				u.setFirstName(rs.getString("firstname"));
				u.setLastName(rs.getString("lastname"));
				
				u.setRole(r);
				
				users.add(u);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		return users;
	}

	@Override
	public void updateUser(User u) {
		try (Connection conn = cu.getConnection()){
			conn.setAutoCommit(false);
			String sql = "update usr set username = ?, passwd = ?, email = ?, firstname = ?, lastname = ?, "
					+ "user_role_id = ? where id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, u.getUsername());
			pstmt.setString(2, u.getPassword());
			pstmt.setString(3, u.getEmail());
			pstmt.setString(4, u.getFirstName());
			pstmt.setString(5, u.getLastName());
			pstmt.setInt(6, u.getRole().getRoleId());
			pstmt.setInt(7, u.getUserId());
			
			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void deleteUser(User u) {
		try (Connection conn = cu.getConnection()){
			conn.setAutoCommit(false);
			
			String sql = "delete from person where id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, u.getUserId());
			
			int rowAffected = pstmt.executeUpdate();
			
			if (rowAffected > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public User getUserById(Integer id) {
		User u = null;
		
		try (Connection conn = cu.getConnection()){
			String sql = "select usr.id, usr.username, usr.passwd, usr.firstname, usr.lastname, usr.email, usr.user_role_id, user_role.name" + 
					"as role_name from usr" + 
					"join user_role on user_role_id = user_role.id where id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()){
				u = new User();
				u.setUserId(id);
				u.setEmail(rs.getString("email"));
				u.setFirstName(rs.getString("firstname"));
				u.setLastName(rs.getString("lastname"));
				u.setPassword(rs.getString("passwd"));
				u.setUsername(rs.getString("username"));
				
				Role r = new Role ();
				
				r.setRole(rs.getString("role_name"));
				r.setRoleId(rs.getInt("user_role_id"));
				
				u.setRole(r);
				
				
				sql = "select * from user_account join account on "
						+ "account_id = account.id where user_id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, u.getUserId());
				
				ResultSet rs2 = pstmt.executeQuery();
				
				Set<Account> accounts = new HashSet();
				
				while (rs.next()) {
					Account a = accountDao.getAccountById(rs.getInt("account_id"));
					accounts.add(a);
				}
				
				u.setAccounts(accounts);
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return u;
	}


	@Override
	public User getUserByUsername(String username) {
		User u = null;
		
		try (Connection conn = cu.getConnection()){
			String sql = "select usr.id, usr.username, usr.passwd, usr.firstname, usr.lastname, usr.email, usr.user_role_id, user_role.name" + 
					"as role_name from usr" + 
					"join user_role on user_role_id = user_role.id where username = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()){
				u = new User();
				u.setUserId(rs.getInt("id"));
				u.setEmail(rs.getString("email"));
				u.setFirstName(rs.getString("firstname"));
				u.setLastName(rs.getString("lastname"));
				u.setPassword(rs.getString("passwd"));
				u.setUsername("username");
				
				Role r = new Role ();
				
				r.setRole(rs.getString("role_name"));
				r.setRoleId(rs.getInt("user_role_id"));
				
				u.setRole(r);
				
				sql = "select * from user_account join account on "
						+ "account_id = account.id where user_id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, u.getUserId());
				
				ResultSet rs2 = pstmt.executeQuery();
				
				Set<Account> accounts = new HashSet();
				
				while (rs.next()) {
					Account a = accountDao.getAccountById(rs.getInt("account_id"));
					accounts.add(a);
				}
				
				u.setAccounts(accounts);
				

				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return u;
	}



}
