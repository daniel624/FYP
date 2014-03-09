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

public class AddPublication {
	static String colName = "";
	static String colValue = "";

	public static void AddColNameValue(String[] inputArray, String compareString)
	{
		if (inputArray[0].equals(compareString)) {
			if (colName != "") {
				colName = colName + ", " + compareString;
				colValue = colValue + ", \"" + inputArray[1] + "\"";
			} else {
				colName = compareString;
				colValue = colValue + "\"" + inputArray[1] + "\"";
			}
		}
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException
	{
		DBConnection db = new DBConnection();
		Connection conn = db.getConnection();
		ResultSet rs = null;
		String sql = "";
		
		String data = "";
		String buffer = "";

		String[] array;
		int counter = 1;
		
		try 
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("src/data/publication/Publication.txt"), "UTF8"));

			buffer = in.readLine();
			while (buffer != null)
			{
				if (buffer.length() != 0)
				{
					if (buffer.contains("=") == true) {
						array = buffer.split("=");
						array = CommonFunction.trimArray(array);

						AddColNameValue(array, "Authors");
						AddColNameValue(array, "BookTitle");
						AddColNameValue(array, "Journal");
						AddColNameValue(array, "Volume");
						AddColNameValue(array, "Number");
						AddColNameValue(array, "Year");
						AddColNameValue(array, "Page");
						AddColNameValue(array, "Proceeding");
						AddColNameValue(array, "Issue");
						AddColNameValue(array, "Article");
						AddColNameValue(array, "Month");
						AddColNameValue(array, "Thesis");
						AddColNameValue(array, "Chapter");
						AddColNameValue(array, "Title");
						AddColNameValue(array, "Editors");
						AddColNameValue(array, "Publisher");
					} else {
						//System.out.println(buffer);
					}
				}
				else
				{
					// empty line
					
					sql = "insert into Publication (" + colName + ") values ("+ colValue + ");";
					System.out.println("sql - " + counter + " = " + sql);
					counter = counter + 1;
					
					try
					{
						db.updateTable(conn, sql);
					}
					catch (Exception e)
					{
						System.out.println("[addSampleData] Exception");
						e.printStackTrace();
					}
					
					colName = "";
					colValue = "";
					
				}
				buffer = in.readLine();
			}
			
			db.closeConnection(conn);
		}
		catch (IOException e)
		{
			System.out.println("IOException!");
			e.printStackTrace();
		}
	}
}
