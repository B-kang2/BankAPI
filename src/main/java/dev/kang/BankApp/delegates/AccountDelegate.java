package dev.kang.BankApp.delegates;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.kang.BankApp.beans.Account;
import dev.kang.BankApp.data.AccountPostgres;
import dev.kang.BankApp.data.AccountStatusPostgres;
import dev.kang.BankApp.data.AccountTypePostgres;
import dev.kang.BankApp.data.RolePostgres;
import dev.kang.BankApp.data.UserPostgres;
import dev.kang.BankApp.services.UserService;
import dev.kang.BankApp.services.AccountService;
import dev.kang.BankApp.delegates.FrontControllerDelegate;

/*
 * /account/withdraw -(POST) withdraws balance from account
 * /account/deposit -(POST) deposit balance into account
 * /account/transfer -(POST) transfer balance to another account
 * 
 *  /account - (GET) finds an account
 *  /account/:id -(GET) finds an account by ID
 *  /account/status/:statusId -(GET) finds accounts by status
 *  /account/owner/:userId -(GET) finds accounts by user
 *  /account -(POST) submits account
 *  		 -(PUT) updates account
 */


public class AccountDelegate implements FrontControllerDelegate{
	private UserService uServ = 
			new UserService(new UserPostgres(), new RolePostgres(), new AccountPostgres());
	private AccountService aServ = 
			new AccountService (new UserPostgres(), new AccountPostgres(), new AccountStatusPostgres(), new AccountTypePostgres());
	private ObjectMapper om = new ObjectMapper();

	public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = (String) req.getAttribute("path");
		
		if (path == null || path.equals("")) {
			Account a = null;
			switch (req.getMethod()) {
				case "GET":
					Set<Account> accounts = aServ.getAccounts();
					resp.getWriter().write(om.writeValueAsString(accounts));					
					break;
				case "POST":
					a = om.readValue(req.getInputStream(), Account.class);
					aServ.addAccount(a);
					resp.getWriter().write(om.writeValueAsString(a));
					break;
				case "PUT":
					
					break;
				default:
					resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
					break;
			}
			
		} else if (path.equals("withdraw")) {
			String accId = req.getParameter("id");
			String balance = req.getParameter("balance");
			
			Account a = new Account();
			Account a = aServ.withdrawBalance(id, balance);
			
			resp.getWriter().write(om.writeValueAsString(a));
			
		} else if (path.equals("deposit")) {
			String accId = req.getParameter("id");
			String balance = req.getParameter("balance");
			
			Account a = aServ.depositBalance(id, balance);;
			
			resp.getWriter().write(om.writeValueAsString(a));
			
		} else if (path.equals("transfer")) {
			String accId1 = req.getParameter("id1");
			String accId2 = req.getParameter("id2");
			String balance = req.getParameter("balance");
			
			aServ.transferBalance(id1, id2, balance);
			
			resp.getWriter().write("");
			
		} else if (path.equals("status")){
			

		} else if (path.equals("owner")){
			
		} else {
			int accountId = Integer.valueOf(path);
			Account a = null;
			switch (req.getMethod()) {
				case "GET":
					break;
				default:
					resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
					break;
			}
		}
		
	}

}
