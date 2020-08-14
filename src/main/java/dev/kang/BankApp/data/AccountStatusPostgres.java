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

import dev.kang.BankApp.beans.AccountStatus;

public class AccountStatusPostgres implements AccountStatusDAO{
	private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();

	@Override
	public Integer createStatus(AccountStatus as) {
		Integer id = 0;
		
		try (Connection conn = cu.getConnection()){
			conn.setAutoCommit(false);
			String sql = "insert into status values (default, ?)";
			String [] keys = {"id"};
			PreparedStatement pstmt = conn.prepareStatement(sql, keys);
			pstmt.setString(1, as.getStatus());
			
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
	public AccountStatus getStatusById(Integer id) {
		AccountStatus s = new AccountStatus();
		
		try (Connection conn = cu.getConnection()){
			String sql = "select name, id from status where id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				s.setStatusId(rs.getInt("id"));
				s.setStatus(rs.getString("name"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	@Override
	public Set<AccountStatus> getStatus() {
		Set<AccountStatus> s = new HashSet<>();
		try (Connection conn = cu.getConnection()){
			String sql = "select * from status";
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				AccountStatus status = new AccountStatus();
				status.setStatusId(rs.getInt("id"));
				status.setStatus(rs.getString("name"));
				s.add(status);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return s;
	}

	@Override
	public void updateStatus(AccountStatus as) {
		try (Connection conn = cu.getConnection()){
			conn.setAutoCommit(false);
			String sql = "update status set name = ? where id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, as.getStatus());
			pstmt.setInt(2, as.getStatusId());
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
	public void deleteStatus(AccountStatus as) {
		try (Connection conn = cu.getConnection()){
			conn.setAutoCommit(false);
			
			String sql = "delete from status where id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, as.getStatusId());
			
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
