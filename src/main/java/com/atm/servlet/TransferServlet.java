package com.atm.servlet;

import java.io.IOException;

import com.atm.dao.AccountDAO;
import com.atm.model.Account;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/transfer")
public class TransferServlet extends HttpServlet {

    private AccountDAO accountDAO = new AccountDAO();

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
                          throws IOException {

        // Get existing session
        HttpSession session =
                request.getSession(false);

        // Check login
        if (session == null ||
            session.getAttribute("account") == null) {

            response.sendRedirect("login.html");
            return;
        }


        // Get logged-in sender
        Account sender =
                (Account) session.getAttribute("account");


        // Get values from form
        String receiverAccountString =
                request.getParameter("receiverAccount");

        String amountString =
                request.getParameter("amount");

        String pinString =
                request.getParameter("pin");


        System.out.println("===== TRANSFER REQUEST =====");

        System.out.println(
                "Sender Account = "
                + sender.getAccountNumber()
        );

        System.out.println(
                "Receiver Account = "
                + receiverAccountString
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

            // Convert values

            int receiverAccount =
                    Integer.parseInt(receiverAccountString);

            double amount =
                    Double.parseDouble(amountString);

            int pin =
                    Integer.parseInt(pinString);


            // Check amount

            if (amount <= 0) {

                response.sendRedirect(
                        "dashboard.jsp?transfer=invalid"
                );

                return;
            }


            // Sender cannot send to himself

            if (receiverAccount ==
                sender.getAccountNumber()) {

                response.sendRedirect(
                        "dashboard.jsp?transfer=self"
                );

                return;
            }


            // Check PIN

            if (pin != sender.getPin()) {

                response.sendRedirect(
                        "dashboard.jsp?transfer=pin"
                );

                return;
            }


            // Check balance

            if (amount > sender.getBalance()) {

                response.sendRedirect(
                        "dashboard.jsp?transfer=balance"
                );

                return;
            }


            // Perform transfer

            boolean success =
                    accountDAO.transferMoney(
                            sender.getAccountNumber(),
                            receiverAccount,
                            amount
                    );


            if (success) {

                // Update sender's session balance

                sender.setBalance(
                        sender.getBalance() - amount
                );

                session.setAttribute(
                        "account",
                        sender
                );


                System.out.println(
                        "TRANSFER SUCCESSFUL"
                );


                // DIRECTLY GO TO DASHBOARD

                response.sendRedirect(
                        "dashboard.jsp?transfer=success"
                );

            } else {

                System.out.println(
                        "TRANSFER FAILED"
                );

                response.sendRedirect(
                        "dashboard.jsp?transfer=failed"
                );
            }


        } catch (NumberFormatException e) {

            e.printStackTrace();

            response.sendRedirect(
                    "dashboard.jsp?transfer=invalid"
            );
        }
    }
}