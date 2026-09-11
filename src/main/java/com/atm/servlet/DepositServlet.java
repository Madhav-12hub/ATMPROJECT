package com.atm.servlet;

import java.io.IOException;

import com.atm.dao.AccountDAO;
import com.atm.model.Account;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/deposit")
public class DepositServlet extends HttpServlet{
	
	 private AccountDAO accountDAO = new AccountDAO();
	 
	 protected void doPost(HttpServletRequest request,
             HttpServletResponse response)
             throws IOException {
		 
		  HttpSession session = request.getSession(false);
		  
		  // Check whether user is logged in
		  
		  if (session == null || session.getAttribute("account") == null) {
	            response.sendRedirect("login.html");
	            return;
	        }

		  Account account = (Account) session.getAttribute("account");
		  
		  String amountString = request.getParameter("amount");
		  String pin=request.getParameter("pin");
		  
		  
		  
		  try {

	            double amount = Double.parseDouble(amountString);

	            if (amount <= 0) {
	                response.sendRedirect("deposit.html?error=invalid");
	                return;
	            }
	            
	            
	            if(pin==null || pin.isEmpty()) {
	            	response.sendRedirect("deposit.html?error=pin");
	            	return;
	            }

	            boolean validPin = accountDAO.validatePin(
	                    account.getAccountNumber(),
	                    pin
	            );
	           
	            
	            
	            if (!validPin) {

	                System.out.println("Invalid PIN");

	                response.sendRedirect("deposit.html?error=pin");
	                return;
	            }
	            
	            boolean success = accountDAO.deposit(
	                    account.getAccountNumber(),
	                    amount
	            );


	            if (success) {

	                // Update session balance
	                account.setBalance(account.getBalance() + amount);

	                session.setAttribute("account", account);

	                response.sendRedirect("dashboard.jsp?deposit=success");

	            } else {

	                response.sendRedirect("deposit.html?error=failed");
	            }

	        } catch (NumberFormatException e) {

	            response.sendRedirect("deposit.html?error=invalid");
	        }
		 
	 }

}
