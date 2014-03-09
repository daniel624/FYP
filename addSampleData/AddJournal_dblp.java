package addSampleData;

import java.io.*;
import java.sql.*;

import db.*;

public class AddJournal_dblp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBConnection db = new DBConnection();
		Connection conn = db.getConnection();
		String filename = "";
		String tmp = "";
		String fullname, shortname;
		String sql = "";
		String sql2 = "";
		ResultSet rs = null;
		
		try {
			for (int i=1; i<26; i++) {
				filename = "src/data/journal/dblp/journal-" + (char) (97+i) + ".txt";
				BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
				
				while (null != (tmp = buf.readLine())) {
					
					if (tmp.contains(",")) {
						fullname = tmp.substring(0, tmp.lastIndexOf(",")).trim();
						shortname = tmp.substring(tmp.lastIndexOf(",")+1).trim();
						
						if (shortname.trim().equals("")) {
							sql2 = "select * from journal where fullname =\"" + fullname + "\"";
							sql = "insert into Journal(FullName,ShortName) values(\"" + fullname + "\", null);";
						}
						else {
							sql2 = "select * from journal where fullname =\"" + fullname + "\"" + "and (shortname =\"" + shortname + "\"" + " or shortname2 =\"" + shortname + "\")";
							sql = "insert into Journal(FullName,ShortName) values(\"" + fullname + "\", \"" + shortname + "\");";
						}
						
						
						System.out.println("sql2" + " = " + sql2);
						rs = db.getResultSet(conn, sql2);
						if (!(rs.next())) {
							db.updateTable(conn, sql);
							System.out.println(sql);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
