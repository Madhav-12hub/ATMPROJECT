package com.atm.servlet;

import java.io.IOException;

import com.atm.dao.AccountDAO;
import com.atm.model.Account;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/withdraw")
public class WithdrawServlet extends HttpServlet {

    private AccountDAO accountDAO = new AccountDAO();

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
                          throws IOException {

        // Get existing session

        HttpSession session =
                request.getSession(false);


        // Check whether user is logged in

        if (session == null ||
            session.getAttribute("account") == null) {

            response.sendRedirect("login.html");

            return;
        }


        // Get logged-in account

        Account account =
                (Account) session.getAttribute("account");


        // Get form values

        String amountString =
                request.getParameter("amount");

        String pinString =
                request.getParameter("pin");


        System.out.println("===== WITHDRAW REQUEST =====");

        System.out.println(
                "Account Number = "
                + account.getAccountNumber()
        );

        System.out.println(
                "Amount = "
                + amountString
        );

        System.out.println(
                "PIN received = "
                + pinString
        );


        try {

            double amount =
                    Double.parseDouble(amountString);

            int pin =
                    Integer.parseInt(pinString);


            // Check amount

            if (amount <= 0) {

                response.sendRedirect(
                        "dashboard.jsp?withdraw=invalid"
                );

                return;
            }


            // Check PIN

            if (pin != account.getPin()) {

                response.sendRedirect(
                        "dashboard.jsp?withdraw=pin"
                );

                return;
            }


            // Check balance

            if (amount > account.getBalance()) {

                response.sendRedirect(
                        "dashboard.jsp?withdraw=balance"
                );

                return;
            }


            // Withdraw money from database

            boolean success =
                    accountDAO.withdraw(
                            account.getAccountNumber(),
                            amount
                    );


            if (success) {

                // Update session balance

                account.setBalance(
                        account.getBalance() - amount
                );

                session.setAttribute(
                        "account",
                        account
                );


                System.out.println(
                        "WITHDRAW SUCCESSFUL"
                );


                // Directly return to dashboard

                response.sendRedirect(
                        "dashboard.jsp?withdraw=success"
                );


            } else {

                System.out.println(
                        "WITHDRAW FAILED"
                );

                response.sendRedirect(
                        "dashboard.jsp?withdraw=failed"
                );
            }


        } catch (NumberFormatException e) {

            e.printStackTrace();

            response.sendRedirect(
                    "dashboard.jsp?withdraw=invalid"
            );
        }

    }

}