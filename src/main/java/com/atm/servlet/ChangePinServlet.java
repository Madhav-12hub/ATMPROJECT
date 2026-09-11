package com.atm.servlet;

import java.io.IOException;

import com.atm.dao.AccountDAO;
import com.atm.model.Account;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/change-pin")
public class ChangePinServlet extends HttpServlet {

    private AccountDAO accountDAO = new AccountDAO();

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
                          throws IOException {

        HttpSession session = request.getSession(false);

        // Check login
        if (session == null ||
            session.getAttribute("account") == null) {

            response.sendRedirect("login.html");
            return;
        }

        Account account =
                (Account) session.getAttribute("account");


        // Get PIN values

        String currentPin =
                request.getParameter("currentPin");

        String newPin =
                request.getParameter("newPin");

        String confirmPin =
                request.getParameter("confirmPin");


        System.out.println("===== CHANGE PIN REQUEST =====");

        System.out.println(
            "Account Number = "
            + account.getAccountNumber()
        );


        // Check PIN length

        if (currentPin == null ||
            newPin == null ||
            confirmPin == null ||
            newPin.length() != 4 ||
            confirmPin.length() != 4) {

            response.sendRedirect(
                "change-pin.html?error=invalid"
            );

            return;
        }


        // Check whether new PINs match

        if (!newPin.equals(confirmPin)) {

            System.out.println(
                "New PIN and Confirm PIN do not match"
            );

            response.sendRedirect(
                "change-pin.html?error=mismatch"
            );

            return;
        }


        // Check current PIN

        boolean validPin =
                accountDAO.validatePin(
                    account.getAccountNumber(),
                    currentPin
                );


        if (!validPin) {

            System.out.println(
                "Current PIN is incorrect"
            );

            response.sendRedirect(
                "change-pin.html?error=wrongpin"
            );

            return;
        }


        // Update PIN in database

        boolean success =
                accountDAO.changePin(
                    account.getAccountNumber(),
                    newPin
                );


        if (success) {

            System.out.println(
                "PIN changed successfully"
            );

            // Update session account PIN if your
            // Account class has setPin()

            int newPinValue = Integer.parseInt(newPin);
            account.setPin(newPinValue);

            session.setAttribute(
                "account",
                account
            );


            // Directly return to dashboard

            response.sendRedirect(
                "dashboard.jsp?pin=success"
            );

        } else {

            System.out.println(
                "PIN change failed"
            );

            response.sendRedirect(
                "change-pin.html?error=failed"
            );
        }
    }
}