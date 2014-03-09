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


public class AddJournal_PubMed {
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
		
			fileName = "src/data/journal/PubMed.txt";
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
						if (buffer.contains(",") == true) {
							//array = buffer.split(",");
							array[0] = buffer.substring(0, buffer.lastIndexOf(","));
							
							if (buffer.substring(buffer.lastIndexOf(",")+1).length() > 1) {
								array[1] = buffer.substring(buffer.lastIndexOf(",")+1);
							} else {
								array[1] = "";
							}
							
							array = CommonFunction.trimArray(array);
							
							colName = "FullName";
							colValue = "\"" + array[0] + "\"";
							sql2 = "select * from journal where fullname =\"" + array[0] + "\"";
							
							if (array[1] != "") {
								colName = colName + ", ShortName";
								colValue = colValue + ", " + "\"" + array[1] + "\"";
								sql2 = sql2 + "and (shortname =\"" + array[1] + "\"" + " or shortname2 =\"" + array[1] + "\")";
							}
							System.out.println("sql2 - " + counter + " = " + sql2);

							try {
								rs = db.getResultSet(conn, sql2);
								if (!(rs.next())) {
			
									sql = "insert into Journal (" + colName + ") values ("+ colValue + ");";
									System.out.println("sql - " + counter + " = " + sql);

									
									db.updateTable(conn, sql);
									
								}
							} catch (Exception e) {
								System.out.println("[addJournal_PubMed] Exception");
								e.printStackTrace();
							}
							
							counter = counter + 1;
							colName = "";
							colValue = "";
						} else {
							System.out.println(counter + "************** Wrong Line **************");
							counter = counter + 1;
						}
					} else {
						// empty line
						System.out.println(counter + "************** Wrong Line **************");
						counter = counter + 1;
					}
					buffer = in.readLine();
				}
			} catch (IOException e)
			{
				System.out.println("IOException!");
				e.printStackTrace();
			}
		
		db.closeConnection(conn);
	}
}
