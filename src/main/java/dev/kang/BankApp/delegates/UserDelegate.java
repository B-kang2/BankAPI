package dev.kang.BankApp.delegates;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dev.kang.BankApp.beans.Role;
import dev.kang.BankApp.data.AccountPostgres;
import dev.kang.BankApp.data.RolePostgres;
import dev.kang.BankApp.data.UserPostgres;
import dev.kang.BankApp.services.UserService;


/*
 * /users -(GET) retrieves all users
 * 		  -(PUT) updates a user
 * /users /:id - (GET) gets user by id
 * 
 */

public class UserDelegate implements FrontControllerDelegate{
	private UserService uServ = new UserService(new UserPostgres(), new RolePostgres(), new AccountPostgres());

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = (String) req.getAttribute("path"); //represents identifier

		if (path == null || path.equals("")) {
			switch (req.getMethod()) {
			case "GET":
				Set<Role> roles = uServ.getRoles();
				
			
			}
		}
		
		
	}

}
