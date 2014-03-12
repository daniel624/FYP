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
public class Main extends HttpServlet {
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


		
		webpage_out.println("<html>");
		webpage_out.println("<head>");
		webpage_out.println("<Script>");
		webpage_out.println("function clearFields(){");
		webpage_out.println("document.getElementById(\"intput_text_id\").value=\"\";"); 
		webpage_out.println("return false;"); 
		webpage_out.println("}");
		webpage_out.println("</Script>");
		webpage_out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
		webpage_out.println("<title>Automatic bibliography field recognition and format conversion</title>");
		webpage_out.println("</head>");
		webpage_out.println("<body style=\"font-family:verdana, sans-serif;\">");
			webpage_out.println("<font align=\"center\" style=\"color: #5858FA\" size=\"5\"><b>Automatic bibliography field recognition and format conversion</b></font>");
			webpage_out.println("<hr>");
			
			webpage_out.println("<table>");
				webpage_out.println("<tr>");
					webpage_out.println("<td>");
						webpage_out.println("<form action=\"Main\" method=\"post\">");
							webpage_out.println("<font color=\"red\" size=\"3\">Type in the raw data in the below given text area and click on Execute button:</font>");
							webpage_out.println("<br/><br/>");

							webpage_out.println("<textarea rows=\"15\" cols=\"50\" name=\"intput_text\" id=\"intput_text_id\">");
							webpage_out.print(intput_text);
							webpage_out.println("</textarea><br><br>");

							webpage_out.println("<input type=\"submit\" value=\"Execute\">");   
							webpage_out.println("<input type=\"button\" value=\"Reset\" onClick=\"clearFields()\">");
						webpage_out.println("</form>");
					webpage_out.println("</td>");
				webpage_out.println("</tr>");
				
				webpage_out.println("<tr>");
					webpage_out.println("<td>");
						webpage_out.println("<font color=\"red\" size=\"3\">Output result:</font>");
							webpage_out.println("<br/><br/>");
						webpage_out.println("<textarea rows=\"15\" cols=\"50\" name=\"output_text\">");
		
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	webpage_out.println("Start Time = " + sdf.format(cal.getTime()));
    	webpage_out.println();
    	
		int totalFields = 0;
		int wrongFields = 0;
		
		DBConnection db = new DBConnection();
		Connection conn = db.getConnection();
		ResultSet rs = null;
		String sql = "";
		
		String title = null;
		String journal = null;
		String proceeding = null;
		String volume = null;
		String issue = null;
		String number = null;
		String page = null;
		String year = null;
		String authors = null;
		String article = null;
		String month = null;
		
		String thesis = null;
		String chapter = null;
		String editors = null;
		String publisher = null;
		String yearDB = null;
		
		String data = "";
		String buffer = "";
		int count = 1;
		
		String totalCorrect = "";
		int indiWrong = 0;
		
		String resultPrint = "";
		int idDB;
		String authorsDB,titleDB,journalDB,proceedingDB,volumeDB,issueDB,numberDB,articleDB,pageDB,monthDB,thesisDB,chapterDB,editorsDB,publisherDB;
		
		String yearMonthTmp = "";
		
		int totalauthor = 0, wrongauthor = 0;
		int totaltitle = 0, wrongtitle = 0;
		int totaljournal = 0, wrongjournal = 0;
		int totalproceeding = 0, wrongproceeding = 0;
		int totalvolume = 0, wrongvolume = 0;
		int totalissue = 0, wrongissue = 0;
		int totalnumber = 0, wrongnumber = 0;
		int totalarticle = 0, wrongarticle = 0;
		int totalpage = 0, wrongpage = 0;
		int totalmonth = 0, wrongmonth = 0;
		int totalthesis = 0, wrongthesis = 0;
		int totalchapter = 0, wrongchapter = 0;
		int totaleditor = 0, wrongeditor = 0;
		int totalpublisher = 0, wrongpublisher = 0;
		int totalyear = 0, wrongyear = 0;
		
		CheckArray ca;
		String tmp;
		
		//"[A-Z]{1}\\." - short form name
		
		// (48) Number: 4 - 5
		
		try 
		{
			BufferedReader in;
			String filename;
			
			filename = "webapps/FYP_UI/WEB-INF/classes/FYP_UI/output.txt";

			in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
			buffer = in.readLine();
			while (buffer != null)
			{
				sql = "";
				
				if (buffer.length() != 0)
				{
					data += buffer + ", ";
				}
				else
				{
					/////////////////////////////////////////////////////////////////
					// Clear the fields
					title = null;
					journal = null;
					proceeding = null;
					volume = null;
					issue = null;
					number = null;
					page = null;
					year = null;
					authors = null;
					article = null;
					month = null;
					
					thesis = null;
					chapter = null;
					editors = null;
					publisher = null;
					/////////////////////////////////////////////////////////////////
					
					webpage_out.println("===== testing string " + count + " =====");
					webpage_out.println(data);
					indiWrong = 0;
					
					webpage_out.println("===== splitted fields " + count + " =====");
					
					tmp = null;
					
					ca = new CheckArray(data);

					/////////////////////////////////////////////////////////////////
					// Identity the fields
					title = ca.getTitlePreSplit();
					
					ca.splitByComma();
					
					thesis = ca.getThesis();

					article = ca.getArticle();
					
					number = ca.getNumber();

					yearMonthTmp = "";
					yearMonthTmp = ca.getYearMonth();
					if (yearMonthTmp != "") {
						year = yearMonthTmp.substring(0, yearMonthTmp.indexOf(","));
						month = yearMonthTmp.substring(yearMonthTmp.indexOf(",") + 1);
					} else {
						year = ca.getYear();
						
						month = ca.getMonth();
					}
					
					volume = ca.getVolume();

					issue = ca.getIssue();
				
					if ((volume == null) || (issue == null) || (page == null)) {
						tmp = ca.getVolumeIssue();
						if (tmp != null) {
							if (tmp.indexOf(",") == tmp.lastIndexOf(",")) {
								if (tmp.substring(0,1).equalsIgnoreCase("y")) {
									if (issue == null) {
										issue = tmp.substring(1, tmp.indexOf(","));
									}
									if (year == null) {
										year = tmp.substring(tmp.indexOf(",") + 1);
									}
								} else {
									if (volume == null) {
										volume = tmp.substring(0, tmp.indexOf(","));
									}
									if (issue == null) {
										issue = tmp.substring(tmp.indexOf(",") + 1);
									}
								}
							} else {
								if (volume == null) {
									volume = tmp.substring(0, tmp.indexOf(","));
								}
								if (issue == null) {
									issue = tmp.substring(tmp.indexOf(",") + 1, tmp.lastIndexOf(","));
								}
								if (page == null) {
									page = tmp.substring(tmp.lastIndexOf(",") + 1);
								}
							}
						}
					}
					
					if (page == null) {
						page = ca.getPage();
					}
					
					if (year == null) {
						year = ca.getYear();
					}
					
					
					
					proceeding = ca.getProceeding();
					
					journal = ca.getJournal();
					
					
					authors = ca.getAuthors();
					
					if (title == null)
					{
						title = ca.getTitlePostSplit();
					}

					publisher = ca.getPublisher();
					

					if ((journal == null) && (title != null)) {
						journal = ca.getJournal_Last_fromTitle(title);
					}
					
					
					
					if (volume == null) {
						volume = ca.getVolumeLast();
					}
					
					if (volume == null) {
						volume = ca.getVolumeLast2_NumberOnly();
					}
					
					if (number == null) {
						number = ca.getNumberLast();
					}
					
					if ((year == null) && (title != null)) {
						year = ca.getYearLast(title);
					}
					
					/////////////////////////////////////////////////////////////////
					// Trim and Replace the field
					if (title != null) title = title.trim();
					if (journal != null) journal = journal.trim();
					if (proceeding != null) proceeding = proceeding.trim();
					if (volume != null) volume = volume.trim();
					if (volume != null) volume = volume.replaceAll(" ", "");
					if (issue != null) issue = issue.trim();
					if (issue != null) issue = issue.replaceAll(" ", "");
					if (number != null) number = number.trim();
					if (number != null) number = number.replaceAll(" ", "");
					if (page != null) page = page.trim();
					if (page != null) page = page.replaceAll(" ", "");
					if (year != null) year = year.trim();
					if (year != null) year = year.replaceAll(" ", "");
					if (authors != null) authors = authors.trim();
					if (article != null) article = article.trim();
					if (month != null) month = month.trim();
					if (month != null) month = month.replaceAll(" ", "");
					
					if (thesis != null) thesis = thesis.trim();
					if (chapter != null) chapter = chapter.trim();
					if (editors != null) editors = editors.trim();
					if (publisher != null) publisher = publisher.trim();

					/////////////////////////////////////////////////////////////////
					// Print out the field
					if (title != null) webpage_out.println("Title = " + title);
					if (journal != null) webpage_out.println("Journal = " + journal);
					if (proceeding != null) webpage_out.println("Proceeding = " + proceeding);
					if (volume != null) webpage_out.println("Volume = " + volume);
					if (issue != null) webpage_out.println("Issue = " + issue);
					if (number != null) webpage_out.println("Number = " + number);
					if (page != null) webpage_out.println("Page = " + page);
					if (year != null) webpage_out.println("Year = " + year);
					if (authors != null) webpage_out.println("Authors = " + authors);
					if (article != null) webpage_out.println("Article = " + article);
					if (month != null) webpage_out.println("Month = " + month);
					
					if (thesis != null) webpage_out.println("Thesis = " + thesis);
					if (chapter != null) webpage_out.println("Chapter = " + chapter);
					if (editors != null) webpage_out.println("Editors = " + editors);
					if (publisher != null) webpage_out.println("Publisher = " + publisher);
					/////////////////////////////////////////////////////////////////
					
					webpage_out.println("===== end " + count + " =====\n\n");
					
					count++;
					data = "";
				}
				buffer = in.readLine();
			}
			
			in.close();
			db.closeConnection(conn);
		} 
		catch (IOException e)
		{
			webpage_out.println("IOException!");
			e.printStackTrace();
		}
		
    	Calendar cal2 = Calendar.getInstance();
    	cal2.getTime();
    	webpage_out.println();
    	webpage_out.println("End Time = " + sdf.format(cal2.getTime()));
    	webpage_out.println();
    	
    	long timeDifInMilliSec = cal2.getTimeInMillis() - cal.getTimeInMillis();
    	long timeDifSeconds = timeDifInMilliSec / (long) 1000;
    	webpage_out.println("Running Time = " + timeDifSeconds + " seconds");
    	
    	
    	webpage_out.println("</textarea><br><br>");
		webpage_out.println("</td>");
	webpage_out.println("</tr>");
webpage_out.println("</table>");

	webpage_out.println("</body>");
webpage_out.println("</html>");
	}
}
