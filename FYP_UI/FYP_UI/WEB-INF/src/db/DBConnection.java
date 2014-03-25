package db;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.lang.*;

public class DBConnection {

	ResultSet rs;
	Statement stmt;
	
	public Connection getConnection()
	{
		Connection conn = null;
		try
		{
			//System.out.println("[DBConnection.getConnection()] loading db.properties file");
			java.net.URL propertyURL = getClass().getResource("db.properties");
			Properties dbProperties = new Properties();
			FileInputStream fileInput = new FileInputStream(propertyURL.getPath().replaceAll("%20", " "));
			dbProperties.load(fileInput);
			//System.out.println("[DBConnection.getConnection()] db.properties file loaded!");
			
			String driver = dbProperties.getProperty("jdbcdriver");
			String dburl = dbProperties.getProperty("dburl");
			String username = dbProperties.getProperty("username");
			String password = dbProperties.getProperty("password");
			String dbtype = dbProperties.getProperty("dbtype");
			
			/*String driver = "com.mysql.jdbc.Driver";
			String dburl = "jdbc:mysql://localhost:3306/test";
			String username = "root";
			String password = "123456";*/

			fileInput.close();

			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(dburl, username, password);

			//System.out.println("[DBConnection.getConnection()] DB connected!");
		}
		catch (Exception e)
		{
			//System.out.println("[DBConnection.getConnection()] Error:");
			e.printStackTrace();
		}
		return conn;
	}
	
	public ResultSet getResultSet(Connection conn, String query){
		rs = null;
		try {
			//System.out.println("Query: " + query);
			if (stmt==null) stmt=conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return rs;
	}

	public int updateTable(Connection conn, String query){
		int r = 0;
		try {	
			if (stmt==null) stmt=conn.createStatement();
			r = stmt.executeUpdate(query);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return r;
	}
	
	public void closeConnection(Connection conn)
	{
		try
		{
			conn.close();
			//System.out.println("[DBConnection.closeConnection()] connection closed...");
		}
		catch (SQLException e)
		{
			//System.out.println("[DBConnection.closeConnection()] Error:");
			e.printStackTrace();
		}
	}

}
