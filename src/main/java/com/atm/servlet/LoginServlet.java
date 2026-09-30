
package com.atm.servlet;

import java.io.IOException;

import com.atm.dao.AccountDAO;
import com.atm.model.Account;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private AccountDAO accountDAO = new AccountDAO();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
                          throws IOException {

        String accountNumber = request.getParameter("accountNumber");
        String pin = request.getParameter("pin");

        System.out.println("Account Number received: " + accountNumber);
        System.out.println("PIN received: " + pin);

        System.out.println("===== LOGIN REQUEST =====");

        System.out.println("accountNumber parameter = "
                + request.getParameter("accountNumber"));

        System.out.println("pin parameter = "
                + request.getParameter("pin"));
        
        try {

            int accountNo = Integer.parseInt(accountNumber);
            int pinNumber = Integer.parseInt(pin);

            Account account = accountDAO.login(accountNo, pinNumber);

            if (account != null) {

                System.out.println("LOGIN SUCCESSFUL");

                HttpSession session = request.getSession();

                session.setAttribute("account", account);

                response.sendRedirect("dashboard.jsp");

            } else {

                System.out.println("LOGIN FAILED");

                response.sendRedirect("login.html?error=invalid");
            }

        } catch (NumberFormatException e) {

            System.out.println("Invalid account number or PIN");

            response.sendRedirect("login.html?error=invalid");
        }
    }
}