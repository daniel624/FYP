package FYP_UI;

//import pack1.*;

/*
 * database:
 * biomedical - PubMed, systematic
 * CS - DBLP (conference, journal), not so systematic
 * ISI citation report (reuters) - all kind
 * 
 * accuracy @ 2013-10-07 13:45 - 39.28% (262/667)
 */

// isi web of knowledge
//http://admin-apps.webofknowledge.com/JCR/JCR?PointOfEntry=Home&SID=W1tJXu9AXkRKrQRMXR8

import db.*;

import java.lang.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.regex.*;
import java.util.*;
import java.io.*;
import java.sql.*;
import java.sql.Date;

import javax.servlet.http.*;
import javax.servlet.*;


//4,13,18,56,63,69,70,98,99
public class Main_test extends HttpServlet {
	public void doPost (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter webpage_out = res.getWriter();
		String intput_text = req.getParameter("intput_text"); 
	
	
		////////////////////////////////////////////////////////
		//String output_filename = "C:/Program Files/Apache Software Foundation/Tomcat 7.0/webapps/FYP_UI/WEB-INF/classes/FYP_UI/output.txt";
		String output_filename = "webapps/FYP_UI/WEB-INF/classes/FYP_UI/output.txt";
	
		FileWriter file_writer = new FileWriter(output_filename);
		
		file_writer.write(intput_text);
		file_writer.close();
		
		////////////////////////////////////////////////////////

		
		webpage_out.println("<html><head><title>");
		webpage_out.println("Test Webpage");
		webpage_out.println("</title></head><body style=\"font-family:verdana, sans-serif;\">");
	
		webpage_out.println("<font align=\"center\" style=\"color: #5858FA\" size=\"5\"><b>Automatic bibliography field recognition and format conversion</b></font>");
		
		webpage_out.println("<textarea rows=\"15\" cols=\"50\" name=\"intput_text\">");
		webpage_out.println(intput_text);
		webpage_out.println("</textarea><br><br>");
		
    	webpage_out.println("<body></html>");
    	webpage_out.close();
	}
}
