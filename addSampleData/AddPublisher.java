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

public class AddPublisher {
	public static void main(String[] args) throws UnsupportedEncodingException
	{
		DBConnection db = new DBConnection();
		Connection conn = db.getConnection();
		ResultSet rs = null;
		String sql = "";
		
		String data = "";
		String buffer = "";

		String[] array = new String[2];
		String fileName;
		String publisher_get = "";
		int counter;

		fileName = "src/data/Publisher.txt";
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
					publisher_get = buffer.substring(buffer.indexOf(" ")+1);
					
					sql = "insert into publisher (pubname) values (\""+ publisher_get + "\");";
					System.out.println("sql - " + counter + " = " + sql);
					counter = counter + 1;
					
					try
					{
						db.updateTable(conn, sql);
					}
					catch (Exception e)
					{
						System.out.println("[addPublisher] Exception");
						e.printStackTrace();
					}
					
					publisher_get = "";
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
