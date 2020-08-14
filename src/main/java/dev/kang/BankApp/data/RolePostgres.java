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

public class RolePostgres implements RoleDAO{
	private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();

	@Override
	public Integer createRole(Role r) {
		Integer id = 0;
		
		try (Connection conn = cu.getConnection()){
			conn.setAutoCommit(false);
			String sql = "insert into user_role values (default, ?)";
			String [] keys = {"id"};
			PreparedStatement pstmt = conn.prepareStatement(sql, keys);
			
			pstmt.setString(1, r.getRole());
			
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
	public Role getRoleById(Integer id) {
		Role r = new Role ();
		
		try (Connection conn = cu.getConnection()){
			String sql = "select * from user_role where id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				r.setRoleId(rs.getInt("id"));
				r.setRole(rs.getString("name"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}

	@Override
	public Set<Role> getRoles() {
		Set<Role> roles = new HashSet<>();
		try (Connection conn = cu.getConnection()){
			String sql = "select * from user_role";
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				Role r = new Role();
				r.setRoleId(rs.getInt("id"));
				r.setRole(rs.getString("name"));
				roles.add(r);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return roles;
	}

	@Override
	public void updateRole(Role r) {
		try (Connection conn = cu.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "UPDATE user_role "
					+ "SET name = ? "
					+ "WHERE id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, r.getRole());
			pstmt.setInt(2, r.getRoleId());
			
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
	public void deleteRole(Role r) {
		try (Connection conn = cu.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "DELETE user_role "
					+ "WHERE name = ? OR "
					+ "WHERE id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, r.getRole());
			pstmt.setInt(2, r.getRoleId());
						
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

}
