package com.atm.dao;

import java.sql.Connection;

import com.atm.model.Account;
import com.atm.util.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDAO {
	
	public int createAccount(Account account) {
	
	String sql="INSERT INTO ACCOUNTS" + " (Holder_name,pin,balance,wrong_attempt,account_locked)" + "VALUES(?,?,?,?,?)";
	
	try {
		Connection con=DBConnection.getConnection();
		
		PreparedStatement pt=con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
		
		pt.setString(1, account.getHolderName());
		pt.setInt(2,account.getPin());
		pt.setDouble(3, account.getBalance());
		pt.setInt(4, 0);
		pt.setBoolean(5, false);
	
		
		int row=pt.executeUpdate(); // send the sql to mysql
		  System.out.println("Rows inserted: " + row);
		
		if(row>0) {
			
			ResultSet rs=pt.getGeneratedKeys();
			
			if (rs.next()) {

                int accountNumber = rs.getInt(1);
                
                System.out.println(
                        "Generated Account Number: " + accountNumber
                    );

                return accountNumber;
            }
		}
	
	
	}catch(Exception e) {
		 System.out.println("ERROR WHILE CREATING ACCOUNT:");
		return -1;
		
	}
	return -1;
	
	}
	
	public Account login(int accountNumber, int pin) {

	    String sql =
	            "SELECT * FROM accounts " +
	            "WHERE account_number = ? AND pin = ?";

	    try {

	        Connection con =
	                DBConnection.getConnection();

	        PreparedStatement ps =
	                con.prepareStatement(sql);

	        ps.setInt(1, accountNumber);
	        ps.setInt(2, pin);

	        ResultSet rs =
	                ps.executeQuery();


	        if (rs.next()) {

	            Account account = new Account();

	            account.setAccountNumber(
	                    rs.getInt("account_number"));

	            account.setHolderName(
	                    rs.getString("holder_name"));

	            account.setPin(
	                    rs.getInt("pin"));

	            account.setBalance(
	                    rs.getDouble("balance"));

	            return account;
	        }

	    } catch (Exception e) {

	        e.printStackTrace();
	    }

	    return null;
	}
	
	public boolean deposit(int accountNumber, double amount) {

		String updateSql =
	            "UPDATE accounts SET balance = balance + ? " +
	            "WHERE account_number = ?";

	    String balanceSql =
	            "SELECT balance FROM accounts WHERE account_number = ?";

	    String transactionSql =
	            "INSERT INTO transactions " +
	            "(account_number, transaction_type, amount, balance_after, transaction_date) " +
	            "VALUES (?, ?, ?, ?, NOW())";

	    try {

	        Connection con = DBConnection.getConnection();

	        con.setAutoCommit(false);

	        // 1. Update account balance
	        PreparedStatement update =
	                con.prepareStatement(updateSql);

	        update.setDouble(1, amount);
	        update.setInt(2, accountNumber);

	        int rows = update.executeUpdate();

	        if (rows == 0) {
	            con.rollback();
	            return false;
	        }

	        // 2. Get new balance
	        PreparedStatement balanceStatement =
	                con.prepareStatement(balanceSql);

	        balanceStatement.setInt(1, accountNumber);

	        ResultSet rs = balanceStatement.executeQuery();

	        double newBalance = 0;

	        if (rs.next()) {
	            newBalance = rs.getDouble("balance");
	        }

	        // 3. Insert transaction
	        PreparedStatement transaction =
	                con.prepareStatement(transactionSql);

	        transaction.setInt(1, accountNumber);
	        transaction.setString(2, "DEPOSIT");
	        transaction.setDouble(3, amount);
	        transaction.setDouble(4, newBalance);

	        transaction.executeUpdate();

	        // 4. Commit everything
	        con.commit();

	        return true;

	    } catch (Exception e) {

	        e.printStackTrace();

	        return false;
	    }
		
	}
	
public boolean transferMoney(int senderAccount,int receiverAccount,double amount) {

	String deductSql =
            "UPDATE accounts " +
            "SET balance = balance - ? " +
            "WHERE account_number = ? " +
            "AND balance >= ?";

    String creditSql =
            "UPDATE accounts " +
            "SET balance = balance + ? " +
            "WHERE account_number = ?";

    String balanceSql =
            "SELECT balance FROM accounts " +
            "WHERE account_number = ?";

    String transactionSql =
            "INSERT INTO transactions " +
            "(account_number, transaction_type, amount, balance_after, transaction_date) " +
            "VALUES (?, ?, ?, ?, NOW())";

    try {

        Connection con =
                DBConnection.getConnection();

        con.setAutoCommit(false);

      
        // 1. Deduct from senderrrrr
      

        PreparedStatement deduct =
                con.prepareStatement(deductSql);

        deduct.setDouble(1, amount);
        deduct.setInt(2, senderAccount);
        deduct.setDouble(3, amount);

        int senderUpdated =
                deduct.executeUpdate();

        if (senderUpdated == 0) {

            con.rollback();

            return false;
        }

     
        // 2. Add to receiver
      

        PreparedStatement credit =
                con.prepareStatement(creditSql);

        credit.setDouble(1, amount);
        credit.setInt(2, receiverAccount);

        int receiverUpdated =
                credit.executeUpdate();

        if (receiverUpdated == 0) {

            con.rollback();

            return false;
        }

        // 3. Get sender baalance
      

        PreparedStatement senderBalance =
                con.prepareStatement(balanceSql);

        senderBalance.setInt(1, senderAccount);

        ResultSet senderRs =
                senderBalance.executeQuery();

        double senderNewBalance = 0;

        if (senderRs.next()) {
            senderNewBalance =
                    senderRs.getDouble("balance");
        }

       
        // 4. Get receiver balance



        PreparedStatement receiverBalance =
                con.prepareStatement(balanceSql);

        receiverBalance.setInt(1, receiverAccount);

        ResultSet receiverRs =
                receiverBalance.executeQuery();

        double receiverNewBalance = 0;

        if (receiverRs.next()) {
            receiverNewBalance =
                    receiverRs.getDouble("balance");
        }

        // 5. Sender transaction
       

        PreparedStatement senderTransaction =
                con.prepareStatement(transactionSql);

        senderTransaction.setInt(1, senderAccount);
        senderTransaction.setString(
                2,
                "TRANSFER_SENT"
        );
        senderTransaction.setDouble(3, amount);
        senderTransaction.setDouble(
                4,
                senderNewBalance
        );

        senderTransaction.executeUpdate();

       
        // 6. Receiver transaction
       

        PreparedStatement receiverTransaction =
                con.prepareStatement(transactionSql);

        receiverTransaction.setInt(1, receiverAccount);
        receiverTransaction.setString(
                2,
                "TRANSFER_RECEIVED"
        );
        receiverTransaction.setDouble(3, amount);
        receiverTransaction.setDouble(
                4,
                receiverNewBalance
        );

        receiverTransaction.executeUpdate();


        con.commit();

        return true;

    } catch (Exception e) {

        e.printStackTrace();

        return false;
    }
}

public boolean withdraw(int accountNumber, double amount) {
	String updateSql =
            "UPDATE accounts " +
            "SET balance = balance - ? " +
            "WHERE account_number = ? " +
            "AND balance >= ?";

    String balanceSql =
            "SELECT balance FROM accounts WHERE account_number = ?";

    String transactionSql =
            "INSERT INTO transactions " +
            "(account_number, transaction_type, amount, balance_after, transaction_date) " +
            "VALUES (?, ?, ?, ?, NOW())";

    try {

        Connection con = DBConnection.getConnection();

        con.setAutoCommit(false);

        // Deduct money
        PreparedStatement update =
                con.prepareStatement(updateSql);

        update.setDouble(1, amount);
        update.setInt(2, accountNumber);
        update.setDouble(3, amount);

        int rows = update.executeUpdate();

        if (rows == 0) {

            con.rollback();

            return false;
        }

        // Get new balance
        PreparedStatement balanceStatement =
                con.prepareStatement(balanceSql);

        balanceStatement.setInt(1, accountNumber);

        ResultSet rs =
                balanceStatement.executeQuery();

        double newBalance = 0;

        if (rs.next()) {
            newBalance = rs.getDouble("balance");
        }

        // Insert transaction
        PreparedStatement transaction =
                con.prepareStatement(transactionSql);

        transaction.setInt(1, accountNumber);
        transaction.setString(2, "WITHDRAW");
        transaction.setDouble(3, amount);
        transaction.setDouble(4, newBalance);

        transaction.executeUpdate();

        con.commit();

        return true;

    } catch (Exception e) {

        e.printStackTrace();

        return false;
    }


}
public boolean validatePin(int accountNumber, String pin) {

    String sql = "SELECT * FROM accounts WHERE account_number = ? AND pin = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, accountNumber);
        ps.setInt(2, Integer.parseInt(pin));

        ResultSet rs = ps.executeQuery();

        return rs.next();

    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
}

public boolean changePin(int accountNumber, String newPin) {

    String sql =
        "UPDATE accounts SET pin = ? WHERE account_number = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, newPin);
        ps.setInt(2, accountNumber);

        int rows = ps.executeUpdate();

        return rows > 0;

    } catch (Exception e) {

        e.printStackTrace();

    }

    return false;
}
	
}
