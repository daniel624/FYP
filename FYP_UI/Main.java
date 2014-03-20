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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

//4,13,18,56,63,69,70,98,99
public class Main extends HttpServlet {
	public void doGet (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter webpage_out = res.getWriter();
		
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
			
			webpage_out.println("<form action=\"index.jsp\" method=\"post\">");
			webpage_out.println("<table>");
				webpage_out.println("<tr>");
					webpage_out.println("<td align=\"center\">");
						webpage_out.println("<font color=\"red\" size=\"3\">Input data:</font>");
					webpage_out.println("</td>");
					
					webpage_out.println("<td align=\"center\">");
						webpage_out.println("<font color=\"red\" size=\"3\">Output result:</font>");
					webpage_out.println("</td>");
				webpage_out.println("</tr>");
				
				webpage_out.println("<tr>");
					webpage_out.println("<td>");
						webpage_out.println("<textarea rows=\"20\" cols=\"70\" name=\"intput_text\" id=\"intput_text_id\">");
						webpage_out.println("</textarea>");
						webpage_out.println("</td>");
						
					webpage_out.println("<td>");
						webpage_out.println("<textarea rows=\"20\" cols=\"70\" name=\"output_text\" id=\"output_text_id\" disabled=\"disabled\">");
						
				    	webpage_out.println("</textarea>");
				    	webpage_out.println("</td>");
						webpage_out.println("</tr>");
						
						webpage_out.println("<tr>");
							webpage_out.println("<td>");
								webpage_out.println("<input type=\"submit\" value=\"Execute\">");
								webpage_out.println("<input type=\"button\" value=\"Reset\" onClick=\"clearFields()\">");
							webpage_out.println("</td>");
							
							webpage_out.println("<td>");
							webpage_out.println("</td>");
						webpage_out.println("</tr>");
					webpage_out.println("</table>");
					

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
		String intput_text = req.getParameter("intput_text"); 
	
	
		// Storing the input data in a file
		////////////////////////////////////////////////////////
		String input_filename = "webapps/FYP_UI/input.txt";
	
		FileWriter file_writer_input = new FileWriter(input_filename);
		PrintWriter print_writer_input = new PrintWriter(file_writer_input);
		
		print_writer_input.print(intput_text);
		print_writer_input.close();
		////////////////////////////////////////////////////////
		
		
		// Storing the output data in a txt file
		////////////////////////////////////////////////////////
		String output_filename_txt = "webapps/FYP_UI/result.txt";

		FileWriter file_writer_output = new FileWriter(output_filename_txt);
		PrintWriter print_writer_output = new PrintWriter(file_writer_output);
		////////////////////////////////////////////////////////
		
		
		// Storing the output data in a xls file
		////////////////////////////////////////////////////////
		String output_filename_xls = "webapps/FYP_UI/result.xls";

		// Create object of FileOutputStream
		FileOutputStream fout = new FileOutputStream(output_filename_xls);

		// Build the Excel File
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		HSSFWorkbook workBook = new HSSFWorkbook();

		// Create the spreadsheet
		HSSFSheet spreadSheet = workBook.createSheet("result");

		// Create the first row
		HSSFRow row = spreadSheet.createRow((short) 0);

		// Create the cells and write to the file
		HSSFCell cell;

		cell = row.createCell(0);
		cell.setCellValue(new HSSFRichTextString("Input Text"));
		cell = row.createCell(1);
		cell.setCellValue(new HSSFRichTextString("Title"));
		cell = row.createCell(2);
		cell.setCellValue(new HSSFRichTextString("Journal"));
		cell = row.createCell(3);
		cell.setCellValue(new HSSFRichTextString("Proceeding"));
		cell = row.createCell(4);
		cell.setCellValue(new HSSFRichTextString("Volume"));
		cell = row.createCell(5);
		cell.setCellValue(new HSSFRichTextString("Issue"));
		cell = row.createCell(6);
		cell.setCellValue(new HSSFRichTextString("Number"));
		cell = row.createCell(7);
		cell.setCellValue(new HSSFRichTextString("Page"));
		cell = row.createCell(8);
		cell.setCellValue(new HSSFRichTextString("Year"));
		cell = row.createCell(9);
		cell.setCellValue(new HSSFRichTextString("Authors"));
		cell = row.createCell(10);
		cell.setCellValue(new HSSFRichTextString("Article"));
		cell = row.createCell(11);
		cell.setCellValue(new HSSFRichTextString("Month"));
		cell = row.createCell(12);
		cell.setCellValue(new HSSFRichTextString("Thesis"));
		cell = row.createCell(13);
		cell.setCellValue(new HSSFRichTextString("Chapter"));
		cell = row.createCell(14);
		cell.setCellValue(new HSSFRichTextString("Editors"));
		cell = row.createCell(15);
		cell.setCellValue(new HSSFRichTextString("Publisher"));

		
		
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
			
			webpage_out.println("<form action=\"index.jsp\" method=\"post\">");
			webpage_out.println("<table>");
				webpage_out.println("<tr>");
					webpage_out.println("<td align=\"center\">");
						webpage_out.println("<font color=\"red\" size=\"3\">Input data:</font>");
					webpage_out.println("</td>");
					
					webpage_out.println("<td align=\"center\">");
						webpage_out.println("<font color=\"red\" size=\"3\">Output result:</font>");
					webpage_out.println("</td>");
				webpage_out.println("</tr>");
				
				webpage_out.println("<tr>");
					webpage_out.println("<td>");
						webpage_out.println("<textarea rows=\"20\" cols=\"70\" name=\"intput_text\" id=\"intput_text_id\">");
						webpage_out.print(intput_text);
						webpage_out.println("</textarea>");
						webpage_out.println("</td>");
						
					webpage_out.println("<td>");
						webpage_out.println("<textarea rows=\"20\" cols=\"70\" name=\"output_text\" id=\"output_text_id\">");
						
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	
    	webpage_out.println("Start Time = " + sdf.format(cal.getTime()));
    	webpage_out.println();
    	
    	print_writer_output.println("Start Time = " + sdf.format(cal.getTime()));
    	print_writer_output.println();
    	
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
			
			filename = "webapps/FYP_UI/input.txt";

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
					print_writer_output.println("===== testing string " + count + " =====");
					webpage_out.println(data);
					print_writer_output.println(data);
					row = spreadSheet.createRow((short) count);
					cell = row.createCell(0);
					cell.setCellValue(new HSSFRichTextString(data));
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
					
					if (title != null) {
						webpage_out.println("Title = " + title);
						print_writer_output.println("Title = " + title);
						cell = row.createCell(1);
						cell.setCellValue(new HSSFRichTextString(title));
					}
					if (journal != null) {
						webpage_out.println("Journal = " + journal);
						print_writer_output.println("Journal = " + journal);
						cell = row.createCell(2);
						cell.setCellValue(new HSSFRichTextString(journal));
					}
					if (proceeding != null) {
						webpage_out.println("Proceeding = " + proceeding);
						print_writer_output.println("Proceeding = " + proceeding);
						cell = row.createCell(3);
						cell.setCellValue(new HSSFRichTextString(proceeding));
					}
					if (volume != null) {
						webpage_out.println("Volume = " + volume);
						print_writer_output.println("Volume = " + volume);
						cell = row.createCell(4);
						cell.setCellValue(new HSSFRichTextString(volume));
					}
					if (issue != null) {
						webpage_out.println("Issue = " + issue);
						print_writer_output.println("Issue = " + issue);
						cell = row.createCell(5);
						cell.setCellValue(new HSSFRichTextString(issue));
					}
					if (number != null) {
						webpage_out.println("Number = " + number);
						print_writer_output.println("Number = " + number);
						cell = row.createCell(6);
						cell.setCellValue(new HSSFRichTextString(number));
					}
					if (page != null) {
						webpage_out.println("Page = " + page);
						print_writer_output.println("Page = " + page);
						cell = row.createCell(7);
						cell.setCellValue(new HSSFRichTextString(page));
					}
					if (year != null) {
						webpage_out.println("Year = " + year);
						print_writer_output.println("Year = " + year);
						cell = row.createCell(8);
						cell.setCellValue(new HSSFRichTextString(year));
					}
					if (authors != null) {
						webpage_out.println("Authors = " + authors);
						print_writer_output.println("Authors = " + authors);
						cell = row.createCell(9);
						cell.setCellValue(new HSSFRichTextString(authors));
					}
					if (article != null) {
						webpage_out.println("Article = " + article);
						print_writer_output.println("Article = " + article);
						cell = row.createCell(10);
						cell.setCellValue(new HSSFRichTextString(article));
					}
					if (month != null) {
						webpage_out.println("Month = " + month);
						print_writer_output.println("Month = " + month);
						cell = row.createCell(11);
						cell.setCellValue(new HSSFRichTextString(month));
					}
					if (thesis != null) {
						webpage_out.println("Thesis = " + thesis);
						print_writer_output.println("Thesis = " + thesis);
						cell = row.createCell(12);
						cell.setCellValue(new HSSFRichTextString(thesis));
					}
					if (chapter != null) {
						webpage_out.println("Chapter = " + chapter);
						print_writer_output.println("Chapter = " + chapter);
						cell = row.createCell(13);
						cell.setCellValue(new HSSFRichTextString(chapter));
					}
					if (editors != null) {
						webpage_out.println("Editors = " + editors);
						print_writer_output.println("Editors = " + editors);
						cell = row.createCell(14);
						cell.setCellValue(new HSSFRichTextString(editors));
					}
					if (publisher != null) {
						webpage_out.println("Publisher = " + publisher);
						print_writer_output.println("Publisher = " + publisher);
						cell = row.createCell(15);
						cell.setCellValue(new HSSFRichTextString(publisher));
						
					}
					
					
					/////////////////////////////////////////////////////////////////
					
					webpage_out.println("===== end " + count + " =====\n\n");
					print_writer_output.println("===== end " + count + " =====\n\n");
					
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
    	
    	print_writer_output.println();
    	print_writer_output.println("End Time = " + sdf.format(cal2.getTime()));
    	print_writer_output.println();
    	
    	long timeDifInMilliSec = cal2.getTimeInMillis() - cal.getTimeInMillis();
    	long timeDifSeconds = timeDifInMilliSec / (long) 1000;
    	webpage_out.println("Running Time = " + timeDifSeconds + " seconds");
    	
    	print_writer_output.println("Running Time = " + timeDifSeconds + " seconds");
    	
    	webpage_out.println("</textarea>");
    	webpage_out.println("</td>");
		webpage_out.println("</tr>");
		
		webpage_out.println("<tr>");
			webpage_out.println("<td>");
				webpage_out.println("<input type=\"submit\" value=\"Execute\">");
				webpage_out.println("<input type=\"button\" value=\"Reset\" onClick=\"clearFields()\">");
			webpage_out.println("</td>");
			
			webpage_out.println("<td>");
			webpage_out.println("<a href=\"result.bib\">Click to download \"result.bib\"</a><br/>");
			webpage_out.println("<a href=\"result.xls\">Click to download \"result.xls\"</a><br/>");
			webpage_out.println("<a href=\"result.txt\">Click to download \"result.txt\"</a>");
			webpage_out.println("</td>");
		webpage_out.println("</tr>");
	webpage_out.println("</table>");
	

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

	print_writer_output.close();
	
	
	
	workBook.write(outputStream);

	outputStream.writeTo(fout);
	outputStream.close();
	fout.close();
	}
}