package dev.kang.BankApp.delegates;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.kang.BankApp.beans.User;
import dev.kang.BankApp.data.AccountPostgres;
import dev.kang.BankApp.data.RolePostgres;
import dev.kang.BankApp.data.UserPostgres;
import dev.kang.BankApp.services.UserService;

public class LoginDelegate implements FrontControllerDelegate{
	private UserService uServ = new UserService (new UserPostgres(), new RolePostgres(), new AccountPostgres());
	private ObjectMapper om = new ObjectMapper();

	public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = (String) req.getAttribute("path");

		
		if (path == null || path.equals("")) {
			if ("POST".equals(req.getMethod()))
				logIn(req, resp);
			
			else {
				resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				
			}
		} 		
	}
	
	
	private void logIn(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String username = req.getParameter("user");
		String password = req.getParameter("pass");
		
		User u = uServ.findUserByName(username);
		if (u != null) {
			if (u.getPassword().equals(password)) {
				req.getSession().setAttribute("user", u);
				resp.getWriter().write(om.writeValueAsString(u));
			} else {
				resp.sendError(404, "Invalid Credentials.");
			}
		} else {
			resp.sendError(404, "No user found with that username.");
		}
	}


}
