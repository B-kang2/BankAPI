package dev.kang.BankApp.delegates;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.kang.BankApp.data.AccountPostgres;
import dev.kang.BankApp.data.RolePostgres;
import dev.kang.BankApp.data.UserPostgres;
import dev.kang.BankApp.services.UserService;

public class LogoutDelegate implements FrontControllerDelegate{
	private UserService uServ = new UserService (new UserPostgres(), new RolePostgres(), new AccountPostgres());
	private ObjectMapper om = new ObjectMapper();

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = (String) req.getAttribute("path");
		if (path == null || path.equals("")) {
			if ("POST".equals(req.getMethod())) {
				req.getSession().invalidate();
				
			} else {
				resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			}
		} else {
			resp.sendError(400, "There was no user logged into the session");
		}
		
		
	}

}
