package dev.kang.BankApp.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dev.kang.BankApp.delegates.AccountDelegate;
import dev.kang.BankApp.delegates.FrontControllerDelegate;
import dev.kang.BankApp.delegates.LoginDelegate;
import dev.kang.BankApp.delegates.LogoutDelegate;
import dev.kang.BankApp.delegates.RegisterDelegate;
import dev.kang.BankApp.delegates.UserDelegate;




public class RequestHandler {
	private Map<String, FrontControllerDelegate> delegateMap;
	
	{
		delegateMap = new HashMap<String, FrontControllerDelegate>();
		
		delegateMap.put("accounts", new AccountDelegate());
		delegateMap.put("users", new UserDelegate());
		delegateMap.put("login", new LoginDelegate());
		delegateMap.put("logout", new LogoutDelegate());
		delegateMap.put("register", new RegisterDelegate());
	}
	
	public FrontControllerDelegate handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// if the request is an OPTIONS request,
		// we will return an empty delegate
		if ("OPTIONS".equals(req.getMethod())) {
			return (r1, r2) -> {};
		}
		
		// first, we get the URI from the request
		StringBuilder uriString = new StringBuilder(req.getRequestURI());
		
		// next, we get rid of the first part of the URL
		uriString.replace(0, req.getContextPath().length()+1, "");
		// at this point, uriString = cat/4
		
		if (uriString.indexOf("/") != -1) {
			req.setAttribute("path", uriString.substring(uriString.indexOf("/")+1));
			uriString.replace(uriString.indexOf("/"), uriString.length(), "");
			// at this point, uriString = cat
		}
		
		return delegateMap.get(uriString.toString());
	}
}
