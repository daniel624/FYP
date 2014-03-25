package addSampleData;

import java.sql.*;
import common.CommonFunction;
import db.*;
import java.io.*;

public class AddConference_dblp {
	static String colName = "";
	static String colValue = "";
	
	public static void main(String[] args) throws UnsupportedEncodingException
	{
		DBConnection db = new DBConnection();
		Connection conn = db.getConnection();
		String sql = "";
		String buffer = "";
		String[] array;
		String fileName;
		int counter;
		
		// a - z (97 -122)
		for (int i=97; i<=122; i++)
		{
			fileName = "src/data/conference/dblp/conference-" + (char)i + ".txt";
			System.out.println(fileName);
			
			counter = 1;
			
			try 
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
	
				buffer = in.readLine();
				while (buffer != null)
				{
					if (buffer.length() != 0)
					{
						if (buffer.contains("{") == true) {
							array = buffer.split("\\{");
							array = CommonFunction.trimArray(array);
							
							colName = "FullName";
							colValue = "\"" + array[0] + "\"";
							
							if (array.length > 1) {
								colName = colName + ", ShortName";
								colValue = colValue + ", " + "\"" + array[1] + "\"";
							}
	
							sql = "insert into Conference (" + colName + ") values ("+ colValue + ");";
							System.out.println("sql - " + counter + " = " + sql);
							counter = counter + 1;
	
							try
							{
								db.updateTable(conn, sql);
							}
							catch (Exception e)
							{
								System.out.println("[addConference] Exception");
								e.printStackTrace();
							}
							
							colName = "";
							colValue = "";
							
						} else {
							System.out.println(counter + "************** Wrong Line **************");
							counter = counter + 1;
						}
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
		}
		
		// 3
		fileName = "src/data/dblp/conference/conference-" + "3" + ".txt";
		System.out.println(fileName);
		
		counter = 1;
		
		try 
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));

			buffer = in.readLine();
			while (buffer != null)
			{
				if (buffer.length() != 0)
				{
					if (buffer.contains("{") == true) {
						array = buffer.split("\\{");
						array = CommonFunction.trimArray(array);
						
						colName = "FullName";
						colValue = "\"" + array[0] + "\"";
						
						if (array.length > 1) {
							colName = colName + ", ShortName";
							colValue = colValue + ", " + "\"" + array[1] + "\"";
						}

						sql = "insert into Conference (" + colName + ") values ("+ colValue + ");";
						System.out.println("sql - " + counter + " = " + sql);
						counter = counter + 1;

						try
						{
							db.updateTable(conn, sql);
						}
						catch (Exception e)
						{
							System.out.println("[addConference] Exception");
							e.printStackTrace();
						}
						
						colName = "";
						colValue = "";
						
					} else {
						System.out.println(counter + "************** Wrong Line **************");
						counter = counter + 1;
					}
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
