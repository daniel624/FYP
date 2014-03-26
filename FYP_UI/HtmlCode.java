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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import weka.ProcessData;
import weka.TestWeka;

//4,13,18,56,63,69,70,98,99
public class HtmlCode extends HttpServlet {
	public void doGet (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter webpage_out = res.getWriter();
		
		String[] header_string = {"InputText", "Title", "Journal", "Proceeding", "Volume", "Issue", "Number", "Page", "Year", "Authors", "Article", "Month", "Thesis","Chapter", "Editors", "Publisher"};

		webpage_out.println("<html>");
		webpage_out.println("<head>");
		webpage_out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
		webpage_out.println("<title>Automatic bibliography field recognition and format conversion</title>");
		
		webpage_out.println("<Script>");
		webpage_out.println("function submit_func(){");
			webpage_out.println("if (");
				webpage_out.println("(document.htmlcode_form.order0.value == \"-1\") &&");
				webpage_out.println("(document.htmlcode_form.order1.value == \"-1\") &&");
				webpage_out.println("(document.htmlcode_form.order2.value == \"-1\") &&");
				webpage_out.println("(document.htmlcode_form.order3.value == \"-1\") &&");
				webpage_out.println("(document.htmlcode_form.order4.value == \"-1\") &&");
				webpage_out.println("(document.htmlcode_form.order5.value == \"-1\") &&");
				webpage_out.println("(document.htmlcode_form.order6.value == \"-1\") &&");
				webpage_out.println("(document.htmlcode_form.order7.value == \"-1\") &&");
				webpage_out.println("(document.htmlcode_form.order8.value == \"-1\") &&");
				webpage_out.println("(document.htmlcode_form.order9.value == \"-1\") &&");
				webpage_out.println("(document.htmlcode_form.order10.value == \"-1\") &&");
				webpage_out.println("(document.htmlcode_form.order11.value == \"-1\") &&");
				webpage_out.println("(document.htmlcode_form.order12.value == \"-1\") &&");
				webpage_out.println("(document.htmlcode_form.order13.value == \"-1\") &&");
				webpage_out.println("(document.htmlcode_form.order14.value == \"-1\") &&");
				webpage_out.println("(document.htmlcode_form.order15.value == \"-1\")");
			webpage_out.println(")");
				webpage_out.println("alert(\"Please select at least one field.\");");
			webpage_out.println("else");
				webpage_out.println("htmlcode_form.submit();");
		webpage_out.println("}");
		webpage_out.println("</Script>");
		
		webpage_out.println("</head>");
		webpage_out.println("<body style=\"font-family:verdana, sans-serif;\">");
			webpage_out.println("<font align=\"center\" style=\"color: #5858FA\" size=\"5\"><b>Automatic bibliography field recognition and format conversion</b></font>");
			webpage_out.println("<hr>");
			
			
			webpage_out.println("<form name=\"htmlcode_form\" action=\"htmlcode.jsp\" method=\"post\">");
			webpage_out.println("<table>");

webpage_out.println("<tr>");
webpage_out.println("<td colspan=2>");
webpage_out.println("Please select the order of different fields (Total 16 fields):");
webpage_out.println("</td>");
webpage_out.println("</tr>");


webpage_out.println("<tr>");
	webpage_out.println("<td colspan=2><br></td>");
webpage_out.println("</tr>");

for (int i=0; i<header_string.length; i++) {
	webpage_out.println("<tr>");
		webpage_out.println("<td>");
			webpage_out.println("<b>" + header_string[i] + "</b>");
		webpage_out.println("</td>");
		webpage_out.println("<td>");
			webpage_out.println("<select name=\"order" + i +"\">");
				webpage_out.println("<option value=\"-1\"></option>");
				webpage_out.println("<option value=\"0\">1</option>");
				webpage_out.println("<option value=\"1\">2</option>");
				webpage_out.println("<option value=\"2\">3</option>");
				webpage_out.println("<option value=\"3\">4</option>");
				webpage_out.println("<option value=\"4\">5</option>");
				webpage_out.println("<option value=\"5\">6</option>");
				webpage_out.println("<option value=\"6\">7</option>");
				webpage_out.println("<option value=\"7\">8</option>");
				webpage_out.println("<option value=\"8\">9</option>");
				webpage_out.println("<option value=\"9\">10</option>");
				webpage_out.println("<option value=\"10\">11</option>");
				webpage_out.println("<option value=\"11\">12</option>");
				webpage_out.println("<option value=\"12\">13</option>");
				webpage_out.println("<option value=\"13\">14</option>");
				webpage_out.println("<option value=\"14\">15</option>");
				webpage_out.println("<option value=\"15\">16</option>");
			webpage_out.println("</select>");
		webpage_out.println("</td>");
	webpage_out.println("</tr>");
}


webpage_out.println("<tr>");
	webpage_out.println("<td colspan=2><br></td>");
webpage_out.println("</tr>");


				webpage_out.println("<tr>");
							webpage_out.println("<td colspan=2>");
								webpage_out.println("<input type=\"button\" value=\"Create HTML Code\" onclick=\"submit_func()\">");
								//webpage_out.println("<input type=\"button\" value=\"Reset\" onClick=\"clearFields()\">");
							webpage_out.println("</td>");
							
						webpage_out.println("</tr>");
					webpage_out.println("</table>");
					webpage_out.println("</form>");
			
			
			
			webpage_out.println("<hr>");
			webpage_out.println("<div align=\"right\">");
				webpage_out.println("<font color=\"grey\" size=\"1\">");
					webpage_out.println("<a href=\"http://www.cuhk.edu.hk\">CUHK</a> |");
					webpage_out.println("<a href=\"http://www.cse.cuhk.edu.hk\">CSE Department</a> |");
					webpage_out.println("<a href=\"http://www.erg.cuhk.edu.hk\">Engineering Faculty</a> |");
					webpage_out.println("<a href=\"/contact.html\">Contact Us</a> |");
					webpage_out.println("<br/>");
					webpage_out.println("Copyright &copy; 2014 Department of Computer Science and Engineering, The Chinese University of Hong Kong. All rights reserved.");
				webpage_out.println("</font>");
			webpage_out.println("</div>");
				
			webpage_out.println("</body>");
		webpage_out.println("</html>");
	}
	
	public void doPost (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter webpage_out = res.getWriter();

		String[] header_string = {"InputText", "Title", "Journal", "Proceeding", "Volume", "Issue", "Number", "Page", "Year", "Authors", "Article", "Month", "Thesis","Chapter", "Editors", "Publisher"};
		int[] user_order = new int[16];
		int[] sort_order_with_correspond_field = new int[16];
		
		for (int i = 0; i<16; i++) {
			user_order[i] = Integer.parseInt(req.getParameter("order" + i));
			sort_order_with_correspond_field[i] = -1;
		}
		
		for (int i = 0; i<16; i++) {
			if (user_order[i] != -1) {
				sort_order_with_correspond_field[user_order[i]] = i;
			}
		}

		webpage_out.println("<html>");
		webpage_out.println("<head>");
		webpage_out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
		webpage_out.println("<title>Automatic bibliography field recognition and format conversion</title>");
		webpage_out.println("</head>");
		webpage_out.println("<body style=\"font-family:verdana, sans-serif;\">");
			webpage_out.println("<font align=\"center\" style=\"color: #5858FA\" size=\"5\"><b>Automatic bibliography field recognition and format conversion</b></font>");
			webpage_out.println("<hr>");
			
		
		webpage_out.println("<textarea rows=\"20\" cols=\"70\" name=\"htmlcode\" id=\"htmlcode_id\">");
		
		// Print out the html code into textbox
		//////////////////////////////////
		webpage_out.println("<html>");
		webpage_out.println("<head>");
		webpage_out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
		webpage_out.println("<title>HTML Code - Title</title>");
		webpage_out.println("</head>");
		webpage_out.println("<body>");
		
		webpage_out.println("<h1>HTML Code - Header</h1>");
		
		webpage_out.println("<table border=\"1\"><tr>");
		
		for (int i=0; i<sort_order_with_correspond_field.length; i++) {
			if (sort_order_with_correspond_field[i] != -1) {
				webpage_out.println("<td>" + header_string[sort_order_with_correspond_field[i]] +"</td>");
			}
		}
		
		webpage_out.println("</tr>");
		
		try {
		    POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("webapps/FYP_UI/result_html.xls"));
		    HSSFWorkbook wb = new HSSFWorkbook(fs);
		    HSSFSheet sheet = wb.getSheet("result");
		    HSSFRow row;
		    HSSFCell cell;
		    
			HSSFCell[] cell_arr = new HSSFCell[16];

		    int rows; // No of rows
		    rows = sheet.getPhysicalNumberOfRows();

		    int cols = 0; // No of columns
		    int tmp = 0;

		    // This trick ensures that we get the data properly even if it doesn't start from first few rows
		    for(int i = 0; i < 10 || i < rows; i++) {
		        row = sheet.getRow(i);
		        if(row != null) {
		            tmp = sheet.getRow(i).getPhysicalNumberOfCells();
		            if(tmp > cols) cols = tmp;
		        }
		    }

		    for(int r = 1; r < rows; r++) {
		        row = sheet.getRow(r);
		        if(row != null) {

		        	webpage_out.println("<tr>");
		        	
		        	for (int i=0; i<sort_order_with_correspond_field.length; i++) {
		        		if (sort_order_with_correspond_field[i] != -1) {
			        		cell_arr[sort_order_with_correspond_field[i]] = row.getCell((short) sort_order_with_correspond_field[i]);
							if (cell_arr[sort_order_with_correspond_field[i]] != null) {
								webpage_out.println("<td>" + cell_arr[sort_order_with_correspond_field[i]].getStringCellValue() +"</td>");
							} else {
								webpage_out.println("<td></td>");
							}
		        		}
		    		}
					
					webpage_out.println("</tr>");
		        }
		    }
		} catch(Exception ioe) {
		    ioe.printStackTrace();
		}
		
		webpage_out.println("</table>");
		
		webpage_out.println("</body>");
		webpage_out.println("</html>");
		//////////////////////////////////
		
		webpage_out.println("</textarea>");

		
		webpage_out.println("<hr>");
		webpage_out.println("<div align=\"right\">");
			webpage_out.println("<font color=\"grey\" size=\"1\">");
				webpage_out.println("<a href=\"http://www.cuhk.edu.hk\">CUHK</a> |");
				webpage_out.println("<a href=\"http://www.cse.cuhk.edu.hk\">CSE Department</a> |");
				webpage_out.println("<a href=\"http://www.erg.cuhk.edu.hk\">Engineering Faculty</a> |");
				webpage_out.println("<a href=\"/contact.html\">Contact Us</a> |");
				webpage_out.println("<br/>");
				webpage_out.println("Copyright &copy; 2014 Department of Computer Science and Engineering, The Chinese University of Hong Kong. All rights reserved.");
			webpage_out.println("</font>");
		webpage_out.println("</div>");
			
		webpage_out.println("</body>");
	webpage_out.println("</html>");

	}
}
