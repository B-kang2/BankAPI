package dev.kang.BankApp.delegates;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.kang.BankApp.beans.User;
import dev.kang.BankApp.data.AccountPostgres;
import dev.kang.BankApp.data.RolePostgres;
import dev.kang.BankApp.data.UserPostgres;
import dev.kang.BankApp.services.UserService;

public class RegisterDelegate implements FrontControllerDelegate{
		private UserService uServ = 
				new UserService(new UserPostgres(), new RolePostgres(), new AccountPostgres());
		private ObjectMapper om = new ObjectMapper();
	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = (String) req.getAttribute("path");
		
		if (path == null || path.equals("")) {
			if ("POST".equals(req.getMethod())) {
				User u = (User) om.readValue(req.getInputStream(), User.class);
				u.setUserId(uServ.registerUser(u));
				resp.getWriter().write(om.writeValueAsString(u));
				resp.setStatus(201);
				
			} else {
				resp.sendError(400, "Invalid fields");
			}
		}
		
		
	}

}
