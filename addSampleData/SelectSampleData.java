package addSampleData;

import java.lang.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.*;

import common.CommonFunction;
import db.*;

import java.net.*;
import java.io.*;

import org.apache.commons.lang3.*;


public class SelectSampleData {
	public static void main(String[] args) throws UnsupportedEncodingException
	{
		DBConnection db = new DBConnection();
		Connection conn = db.getConnection();
		ResultSet rs = null;
		String sql = "";
		
		String resultPrint = "";
		
		try
		{
			sql = "select * from SampleData where ID = 1";
			rs = db.getResultSet(conn, sql);
			
			while (rs.next())
			{
				resultPrint = "";
				
				int idDB = rs.getInt("ID");
				if (idDB != 0) resultPrint = resultPrint + idDB + "\t";
				String authorsDB = rs.getString("Authors");
				if (authorsDB != null) resultPrint = resultPrint + authorsDB + "\t";
				String titleDB = rs.getString("Title");
				if (titleDB != null) resultPrint = resultPrint + titleDB + "\t";
				String journalDB = rs.getString("Journal");
				if (journalDB != null) resultPrint = resultPrint + journalDB + "\t";
				String proceedingDB = rs.getString("Proceeding");
				if (proceedingDB != null) resultPrint = resultPrint + proceedingDB + "\t";
				String volumeDB = rs.getString("Volume");
				if (volumeDB != null) resultPrint = resultPrint + volumeDB + "\t";
				String issueDB = rs.getString("Issue");
				if (issueDB != null) resultPrint = resultPrint + issueDB + "\t";
				String numberDB = rs.getString("Number");
				if (numberDB != null) resultPrint = resultPrint + numberDB + "\t";
				String articleDB = rs.getString("Article");
				if (articleDB != null) resultPrint = resultPrint + articleDB + "\t";
				String pageDB = rs.getString("Page");
				if (pageDB != null) resultPrint = resultPrint + pageDB + "\t";
				String monthDB = rs.getString("Month");
				if (monthDB != null) resultPrint = resultPrint + monthDB + "\t";
				String thesisDB = rs.getString("Thesis");
				if (thesisDB != null) resultPrint = resultPrint + thesisDB + "\t";
				String chapterDB = rs.getString("Chapter");
				if (chapterDB != null) resultPrint = resultPrint + chapterDB + "\t";
				String bookTitleDB = rs.getString("BookTitle");
				if (bookTitleDB != null) resultPrint = resultPrint + bookTitleDB + "\t";
				String editorsDB = rs.getString("Editors");
				if (editorsDB != null) resultPrint = resultPrint + editorsDB + "\t";
				String publisherDB = rs.getString("Publisher");
				if (publisherDB != null) resultPrint = resultPrint + publisherDB + "\t";
				int yearDB = rs.getInt("Year");
				if (yearDB != 0) resultPrint = resultPrint + yearDB + "\t";
				
				System.out.println(resultPrint);
			}
			
			rs.close();
		}
		catch (SQLException e)
		{
			System.out.println("[selectSampleData] SQLException");
			e.printStackTrace();
		}
		finally
		{
			db.closeConnection(conn);
		}
		
		
	}
}
