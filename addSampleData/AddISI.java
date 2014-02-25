package addSampleData;

import java.sql.*;
import common.CommonFunction;
import db.*;
import java.io.*;
import java.util.*;

public class AddISI {
	static String colName = "";
	static String colValue = "";
	
	public static void main(String[] args) throws UnsupportedEncodingException
	{
		DBConnection db = new DBConnection();
		Connection conn = db.getConnection();
		String sql = "";
		String sql2 = "";
		String buffer = "";
		String[] array;
		String fileName = "src/data/isi/ISI-data.txt";
		int counter = 1;
		HashMap<String, String> map = new HashMap<String, String>();
		int flag;
		
		try 
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF8"));

			buffer = in.readLine();
			while (buffer != null)
			{
				if (buffer.length() != 0)
				{
					array = buffer.split("%%");
					array = CommonFunction.trimArray(array);
					
					colName = "FullName, ShortName, ShortName2";
					colValue = "\"" + array[0] + "\"," + "\"" + array[1] + "\"," + "\"" + array[2] + "\"";

					sql = "insert into Journal (" + colName + ") values ("+ colValue + ");";
					//System.out.println("sql - " + counter + " = " + sql);
					
					flag = 0;
					if (!map.containsKey(array[3])) {
						map.put(array[3], array[3]);
						
						sql2 = "insert into Publisher (pubname) values (\"" + array[3] + "\");";
						//System.out.println("sql - " + counter + " = " + sql);
						flag = 1;
					}

					try
					{
						//db.updateTable(conn, sql);
						if (flag==1) {
							//db.updateTable(conn, sql2);
						}
					}
					catch (Exception e)
					{
						System.out.println("[addConference] Exception");
						e.printStackTrace();
					}
				}
				else
				{
					// empty line
					System.out.println(counter + "************** Empty Line **************");
				}
				counter = counter + 1;
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
