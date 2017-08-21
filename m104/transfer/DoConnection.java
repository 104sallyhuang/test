package com.m104.transfer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.e104.biz.ap.E104GlobalXml;

public class DoConnection {
	public static String APNO = "12345";
	// TODO Auto-generated method stub
	private static E104GlobalXml gx= new E104GlobalXml(APNO);

	
	public static Connection getConnection(String dbname)
			throws ClassNotFoundException, SQLException {
		Connection con;
		String driver = DoConnection.gx.getDb(dbname).getDriver().trim();
		String url =  DoConnection.gx.getDb(dbname).getDatabase().trim();
		String username =  DoConnection.gx.getDb(dbname).getUsername().trim();
		String password =  DoConnection.gx.getDb(dbname).getPassword().trim();
		Class.forName(driver);
		con = DriverManager.getConnection(url, username, password);
		return con;
	}
/*
	public Connection getConnection(String driver, String url, String username, String password)
			throws ClassNotFoundException, SQLException {
		Connection con;
		Class.forName(driver);
		con = DriverManager.getConnection(url, username, password);
		return con;
	}
*/
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
