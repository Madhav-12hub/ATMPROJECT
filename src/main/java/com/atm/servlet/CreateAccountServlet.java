package com.atm.servlet;

import java.io.IOException;

import com.atm.dao.AccountDAO;
import com.atm.model.Account;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/create-account")
public class CreateAccountServlet extends HttpServlet{
	

    private AccountDAO accountDAO = new AccountDAO();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException{
		
		String name= request.getParameter("name");
		
		String pin=request.getParameter("pin");
		
		String confirmPin=request.getParameter("confirmPin");
		
		
		 // Convert String values
		
		int pinNumber =
                Integer.parseInt(pin);

        int confirmPinNumber =
                Integer.parseInt(confirmPin);

        
        // validating this pin if its ryt or wrong!!

        if (pinNumber != confirmPinNumber) {

            response.sendRedirect(
                    "create-account.html?error=pin");

            return;
        }
        
        //vadlidate the deposit
      
        
        
		Account account=new Account();
		account.setHolderName(name);
		
		account.setPin(pinNumber);
		
		//balance is zero initially
		account.setBalance(0);
		
		//save account number into database
		
		
		int accountNumber=accountDAO.createAccount(account);
		
		// check result
		
		if(accountNumber !=-1) {
			response.sendRedirect("account-created.html?account=" + accountNumber);
		}else {
			response.sendRedirect(
                    "create-account.html?error=failed");
		}
		
	}
	
	
	

}
