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


public class AddJournal_elsevier_journal {
	static String colName = "";
	static String colValue = "";
	
	public static void main(String[] args) throws UnsupportedEncodingException
	{
		DBConnection db = new DBConnection();
		Connection conn = db.getConnection();
		ResultSet rs = null;
		String sql = "";
		String sql2 = "";
		
		String data = "";
		String buffer = "";

		String[] array = new String[2];
		String fileName;
		int counter;
		
			fileName = "src/data/journal/elsevier_journal.txt";
			//fileName = "src/data/dblp/journal/journal-" + (char)i + ".txt";
			System.out.println(fileName);
			
			counter = 1;
			
			try 
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF8"));
	
				buffer = in.readLine();
				while (buffer != null)
				{
					if (buffer.length() != 0)
					{
						colName = "FullName";
						colValue = "\"" + buffer + "\"";

						sql = "insert into Journal (" + colName + ") values ("+ colValue + ");";

						sql2 = "select * from journal where fullname =\"" + buffer + "\"";
						
						System.out.println("sql2" + " = " + sql2);
						
						try {
							rs = db.getResultSet(conn, sql2);
							if (!(rs.next())) {
								System.out.println("sql - " + counter + " = " + sql);
								counter = counter + 1;
								
								db.updateTable(conn, sql);
		
							}
						} catch (Exception e) {
							System.out.println("[addJournal_elsevier_journal] Exception");
							e.printStackTrace();
						}
						
						colName = "";
						colValue = "";
					}
					else
					{
						// empty line
						System.out.println(counter + "************** Wrong Line **************");
						counter = counter + 1;
					}
					buffer = in.readLine();
				}
			}
			catch (IOException e)
			{
				System.out.println("IOException!");
				e.printStackTrace();
			}
		
		db.closeConnection(conn);
	}
}
