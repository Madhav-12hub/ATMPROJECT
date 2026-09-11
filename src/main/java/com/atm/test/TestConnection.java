package com.atm.test;
import java.sql.Connection;

import com.atm.util.DBConnection;

public class TestConnection {
	public static void main(String[] args) {

        Connection con = DBConnection.getConnection();

        if (con != null) {
            System.out.println("Connection successful!fireee");
        } else {
            System.out.println("Connection failed!");
        }
    }

}
