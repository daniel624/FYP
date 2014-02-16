package addSampleData;

import java.io.*;
import java.sql.*;
import db.*;

public class AddData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//DBConnection db = new DBConnection();
		//Connection conn = db.getConnection();
		String filename = "";
		String tmp = "";
		String fullname, shortname;
		String sql = "";
		
		try {
			for (int i=1; i<26; i++) {
				filename = "src/data/dblp/journal-" + (char) (97+i) + ".txt";
				BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
				
				while (null != (tmp = buf.readLine())) {
					fullname = tmp.substring(0, tmp.lastIndexOf(",")).trim();
					shortname = tmp.substring(tmp.lastIndexOf(",")+1).trim();
					
					if (shortname.trim().equals(""))
						sql = "insert into Journal(FullName,ShortName) values('" + fullname + "', null);";
					else
						sql = "insert into Journal(FullName,ShortName) values('" + fullname + "', '" + shortname + "');";
					
					System.out.println(sql);
					//db.updateTable(conn, sql);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
