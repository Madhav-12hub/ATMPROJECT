<%@ page import="com.atm.model.Account" %>
<%@ page import="com.atm.dao.TransactionDAO" %>
<%@ page import="com.atm.model.MonthlyTransaction" %>
<%@ page import="java.util.List" %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    // Get the logged-in account from session
    Account account = (Account) session.getAttribute("account");

    // If user is not logged in, send them to login page
    if (account == null) {
        response.sendRedirect("login.html");
        return;
    }
%>

<%
    TransactionDAO transactionDAO = new TransactionDAO();

    List<MonthlyTransaction> monthlyTransactions =
            transactionDAO.getMonthlyTransactions(
                    account.getAccountNumber()
            );
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Dashboard | ATM Bank</title>

    <link rel="stylesheet" href="style.css">

</head>

<body class="dashboard-page">

    <div class="dashboard-container">

        <!-- HEADER -->

        <header class="dashboard-header">

            <div>

                <p class="card-eyebrow">
                    ATM BANK
                </p>

                <h1>
                    Welcome back!
                </h1>

                <p class="dashboard-subtitle">
                    Manage your account securely.
                </p>

            </div>


            <a href="logout"
               class="logout-button">

                Logout

            </a>

        </header>


        <!-- ACCOUNT SUMMARY -->

        <section class="account-summary">


            <!-- ACCOUNT INFORMATION -->

            <div class="account-info">

                <p class="summary-label">
                    ACCOUNT HOLDER
                </p>

                <h2>
                    <%= account.getHolderName() %>
                </h2>

                <p class="account-number">

                    Account Number:
                    <%= account.getAccountNumber() %>

                </p>

            </div>


            <!-- BALANCE -->

            <div class="balance-box">

                <p class="summary-label">
                    AVAILABLE BALANCE
                </p>

                <h2>

                    ₹<%= String.format("%.2f", account.getBalance()) %>

                </h2>

            </div>

        </section>

<!-- ATM OPERATIONS -->

<section class="operations">

    <h2>
        ATM Services
    </h2>

    <p class="section-description">
        Choose an operation to continue.
    </p>

    <div class="operation-grid">

        <!-- DEPOSIT -->

        <div class="operation-card">

            <div class="operation-icon">
                💰
            </div>

            <h3>
                Deposit Money
            </h3>

            <p>
                Add money to your bank account.
            </p>

            <a href="deposit.html"
               class="operation-button">

                Deposit

            </a>

        </div>


        <!-- WITHDRAW -->

        <div class="operation-card">

            <div class="operation-icon">
                💳
            </div>

            <h3>
                Withdraw Money
            </h3>

            <p>
                Withdraw money from your account.
            </p>

            <a href="withdraw.html"
               class="operation-button">

                Withdraw

            </a>

        </div>


        <!-- CHANGE PIN -->

        <div class="operation-card">

            <div class="operation-icon">
                🔐
            </div>

            <h3>
                Change PIN
            </h3>

            <p>
                Change your ATM PIN securely.
            </p>

            <a href="#"
               class="operation-button">

                Change PIN

            </a>

        </div>


        <!-- SEND MONEY -->

        <div class="operation-card">

            <div class="operation-icon">
                💸
            </div>

            <h3>
                Send Money
            </h3>

            <p>
                Transfer money to another account.
            </p>

            <a href="send-money.html"
               class="operation-button">

                Send Money

            </a>

        </div>

    </div>

</section>

<!-- MONTHLY TRANSACTION CHART -->

<!-- TRANSACTION OVERVIEW -->

<section class="transaction-section">

    <h2>Transaction Overview</h2>

    <p class="section-description">
        Monthly deposits and withdrawals
    </p>

    <div class="chart-card">

        

            <div class="chart-title">

                <span>₹</span>

                <span>Transaction Amount</span>


        </div>

        <div class="chart-container">

            <canvas id="transactionChart"></canvas>

        </div>

    </div>

</section>


<!-- CHART.JS -->

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>

const months = [

<%
    for (MonthlyTransaction t : monthlyTransactions) {
%>

    "<%= t.getMonth() %>",

<%
    }
%>

];


const deposits = [

<%
    for (MonthlyTransaction t : monthlyTransactions) {
%>

    <%= t.getDeposit() %>,

<%
    }
%>

];


const withdrawals = [

<%
    for (MonthlyTransaction t : monthlyTransactions) {
%>

    <%= t.getWithdrawal() %>,

<%
    }
%>

];


console.log("Months:", months);
console.log("Deposits:", deposits);
console.log("Withdrawals:", withdrawals);


const ctx =
    document.getElementById("transactionChart");


new Chart(ctx, {

    type: "bar",

    data: {

        labels: months,

        datasets: [

            {
                label: "Deposits",

                data: deposits,

                backgroundColor: '#2563eb',

                borderColor: '#2563eb',

                borderWidth: 1,

                borderRadius: 8,

                barThickness: 35
            },

            {
                label: "Withdrawals",

                data: withdrawals,

                backgroundColor: '#ef4444',

                borderColor: '#ef4444',

                borderWidth: 1,

                borderRadius: 8,

                barThickness: 35
            }

        ]

    },

    options: {

        responsive: true,

        maintainAspectRatio: false,

        interaction: {
            mode: "index",
            intersect: false
        },

        plugins: {

            legend: {

                position: "top",

                labels: {

                    usePointStyle: true,

                    padding: 20,

                    font: {
                        size: 13
                    }

                }

            },

            tooltip: {

                callbacks: {

                    label: function(context) {

                        return " ₹" +
                            context.raw.toLocaleString("en-IN");

                    }

                }

            }

        },

        scales: {

            x: {

                grid: {
                    display: false
                },

                ticks: {

                    color: "#53657d",

                    font: {
                        size: 13
                    }

                }

            },

            y: {

                beginAtZero: true,

                grid: {

                    color: "#e6ebf2"
                },

                ticks: {

                	 color: '#64748b',

                    callback: function(value) {

                        return "₹" +
                            value.toLocaleString("en-IN");

                    }

                }

            }

        }

    }

});

</script>


</body>

</html>