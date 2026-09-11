package com.atm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.atm.model.MonthlyTransaction;
import com.atm.util.DBConnection;

public class TransactionDAO {
	
	public List<MonthlyTransaction> getMonthlyTransactions(int accountNumber) {

	    List<MonthlyTransaction> list = new ArrayList<>();

	    String sql =
	        "SELECT DATE_FORMAT(transaction_date, '%b') AS month, " +
	        "SUM(CASE WHEN transaction_type = 'DEPOSIT' " +
	        "THEN amount ELSE 0 END) AS deposits, " +
	        "SUM(CASE WHEN transaction_type = 'WITHDRAW' " +
	        "THEN amount ELSE 0 END) AS withdrawals " +
	        "FROM transactions " +
	        "WHERE account_number = ? " +
	        "AND transaction_date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH) " +
	        "GROUP BY YEAR(transaction_date), MONTH(transaction_date), " +
	        "DATE_FORMAT(transaction_date, '%b') " +
	        "ORDER BY YEAR(transaction_date), MONTH(transaction_date)";

	    try (
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement(sql)
	    ) {

	        ps.setInt(1, accountNumber);

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {

	            MonthlyTransaction transaction =
	                    new MonthlyTransaction();

	            transaction.setMonth(
	                    rs.getString("month"));

	            transaction.setDeposit(
	                    rs.getDouble("deposits"));

	            transaction.setWithdrawal(
	                    rs.getDouble("withdrawals"));

	            list.add(transaction);
	        }

	    } catch (Exception e) {

	        e.printStackTrace();
	    }

	    return list;
	}

}
