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

public class AccountTypePostgres implements AccountTypeDAO{
	private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();

	@Override
	public Integer createType(AccountType at) {
		Integer id = 0;
		
		try (Connection conn = cu.getConnection()){
			conn.setAutoCommit(false);
			String sql = "insert into TYP values (default, ?)";
			String [] keys = {"id"};
			PreparedStatement pstmt = conn.prepareStatement(sql, keys);
			pstmt.setString(1, at.getType());
			
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
	public AccountType getTypeById(Integer id) {
		AccountType t = new AccountType();
		
		try (Connection conn = cu.getConnection()){
			String sql = "select name, id from TYP where id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				t.setTypeId(rs.getInt("id"));
				t.setType(rs.getString("name"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	@Override
	public Set<AccountType> getTypes() {
		Set<AccountType> t = new HashSet<>();
		try (Connection conn = cu.getConnection()){
			String sql = "select * from typ";
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				AccountType type = new AccountType();
				type.setTypeId(rs.getInt("id"));
				type.setType(rs.getString("name"));
				t.add(type);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return t;
	}

	@Override
	public void updateType(AccountType at) {
		try (Connection conn = cu.getConnection()){
			conn.setAutoCommit(false);
			String sql = "update typ set name = ? where id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, at.getType());
			pstmt.setInt(2, at.getTypeId());
			int rowsAffected = pstmt.executeUpdate();
			
			if(rowsAffected > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void deleteType(AccountType at) {
		try (Connection conn = cu.getConnection()){
			conn.setAutoCommit(false);
			
			String sql = "delete from typ where id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, at.getTypeId());
			
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

}
