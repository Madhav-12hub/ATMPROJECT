package com.atm.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

	static Connection con = null;
	
	  private static final String URL =
	            "jdbc:mysql://localhost:3306/atm_db";

	    private static final String USER = "root";

	    private static final String PASSWORD = "Madhav@24";

	    public static Connection getConnection() {

	        

	        try {

	            Class.forName("com.mysql.cj.jdbc.Driver");

	            con = DriverManager.getConnection(
	                    URL,
	                    USER,
	                    PASSWORD
	            );

	            System.out.println("Database connected successfully!");

	        } catch (Exception e) {

	            e.printStackTrace();
	        }

	        return con;
	    }
}
