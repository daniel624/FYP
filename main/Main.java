package main;

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

//4,13,18,56,63,69,70,98,99
public class Main {
	public static void main(String[] args)
	{
		
		/**
		////////////////////////////////////////////////////////
		// output console result to file
		String output_filename = "src/main/output.txt";
					
		try {
			System.setOut(new PrintStream(new FileOutputStream(output_filename)));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		////////////////////////////////////////////////////////
		 * 
		 */
		
		 
		
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	System.out.println("Start Time = " + sdf.format(cal.getTime()));
    	System.out.println();
    	
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
		
		String data = "";
		String buffer = "";
		int count = 1;
		
		String totalCorrect = "";
		int indiWrong = 0;
		
		String resultPrint = "";
		int idDB,yearDB;
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
			
			// 100 records (4,13,18,56,63,69,70,98,99)
			//filename = "src/data/freelist_100.txt";
			//filename = "src/data/freelist_200.txt";
			filename = "src/data/freelist_380.txt";
			//filename = "src/data/freelist_test.txt";

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
					
					System.out.println("===== testing string " + count + " =====");
					System.out.println(data);
					indiWrong = 0;
					
					System.out.println("===== splitted fields " + count + " =====");
					
					tmp = null;
					
					ca = new CheckArray(data);

					/////////////////////////////////////////////////////////////////
					// Identity the fields
					title = ca.getTitlePreSplit();
					if (title != null) System.out.println("Title = " + title);
					
					ca.splitByComma();
					
					thesis = ca.getThesis();
					if (thesis != null) System.out.println("Thesis = " + thesis);

					article = ca.getArticle();
					if (article != null) System.out.println("Article = " + article);
					
					number = ca.getNumber();
					if (number != null) System.out.println("Number = " + number);
					
					volume = ca.getVolume();
					if (volume != null) System.out.println("Volume = " + volume);

					issue = ca.getIssue();
					if (issue != null) System.out.println("Issue = " + issue);
				
					if ((volume == null) || (issue == null) || (page == null)) {
						tmp = ca.getVolumeIssue();
						if (tmp != null) {
							if (tmp.indexOf(",") == tmp.lastIndexOf(",")) {
								if (volume == null) {
									volume = tmp.substring(0, tmp.indexOf(","));
									System.out.println("Volume = " + volume);
								}
								if (issue == null) {
									issue = tmp.substring(tmp.indexOf(",") + 1);
									System.out.println("Issue = " + issue);
								}
							} else {
								if (volume == null) {
									volume = tmp.substring(0, tmp.indexOf(","));
									System.out.println("Volume = " + volume);
								}
								if (issue == null) {
									issue = tmp.substring(tmp.indexOf(",") + 1, tmp.lastIndexOf(","));
									System.out.println("Issue = " + issue);
								}
								if (page == null) {
									page = tmp.substring(tmp.lastIndexOf(",") + 1);
									System.out.println("Page = " + page);
								}
							}
						}
					}
					
					if (page == null) {
						page = ca.getPage();
						if (page != null) System.out.println("Page = " + page);
					}

					yearMonthTmp = "";
					yearMonthTmp = ca.getYearMonth();
					if (yearMonthTmp != "") {
						year = yearMonthTmp.substring(0, yearMonthTmp.indexOf(","));
						System.out.println("Year = " + year);
						month = yearMonthTmp.substring(yearMonthTmp.indexOf(",") + 1);
						System.out.println("Month = " + month);
					} else {
						year = ca.getYear();
						if (year != null) System.out.println("Year = " + year);
						
						month = ca.getMonth();
						if (month != null) System.out.println("Month = " + month);
					}
					
					
					proceeding = ca.getProceeding();
					if (proceeding != null) System.out.println("Proceeding = " + proceeding);
					
					journal = ca.getJournal();
					if (journal != null) System.out.println("Journal = " + journal);
					
					
					authors = ca.getAuthors();
					if (authors != null) System.out.println("Authors = " + authors);
					
					
					if (title == null)
					{
						title = ca.getTitlePostSplit();
						if (title!=null) System.out.println("Title = " + title);
					}

					publisher = ca.getPublisher();
					if (publisher != null) System.out.println("Publisher = " + publisher);
					
					
					if (volume == null) {
						volume = ca.getVolumeLast();
						if (volume != null) System.out.println("Volume = " + volume);
					}
					
					/////////////////////////////////////////////////////////////////

					if (title != null) title = title.trim();
					if (journal != null) journal = journal.trim();
					if (proceeding != null) proceeding = proceeding.trim();
					if (volume != null) volume = volume.trim();
					if (issue != null) issue = issue.trim();
					if (number != null) number = number.trim();
					if (page != null) page = page.trim();
					if (year != null) year = year.trim();
					if (authors != null) authors = authors.trim();
					if (article != null) article = article.trim();
					if (month != null) month = month.trim();
					
					if (thesis != null) thesis = thesis.trim();
					if (chapter != null) chapter = chapter.trim();
					if (editors != null) editors = editors.trim();
					if (publisher != null) publisher = publisher.trim();

					/////////////////////////////////////////////////////////////////
					
					sql = "select * from Publication where ID = " + count;
					System.out.println();
					//System.out.println(sql);
					rs = db.getResultSet(conn, sql);
					
					while (rs.next())
					{
						resultPrint = "";
						
						idDB = rs.getInt("ID");
						if (idDB != 0) resultPrint = resultPrint + idDB + "\t";
						
						authorsDB = rs.getString("Authors");
						if (authorsDB != null) {
							totalFields = totalFields + 1;
							totalauthor++;
							if (!(authorsDB.equals(authors))) {
								 System.out.println("***Wrong Authors");
								 wrongFields = wrongFields + 1;
								 indiWrong++;
								 wrongauthor++;
							}
						}
						
						titleDB = rs.getString("Title");
						if (titleDB != null) {
							totalFields = totalFields + 1;
							totaltitle++;
							if (!(titleDB.equals(title))) {
								 System.out.println("***Wrong Title");
								 wrongFields = wrongFields + 1;
								 indiWrong++;
								 wrongtitle++;
							}
						}
						
						journalDB = rs.getString("Journal");
						if (journalDB != null) {
							totalFields = totalFields + 1;
							totaljournal++;
							if (!(journalDB.equals(journal))) {
								 System.out.println("***Wrong Journal");
								 wrongFields = wrongFields + 1;
								 indiWrong++;
								 wrongjournal++;
							}
						}
						
						proceedingDB = rs.getString("Proceeding");
						if (proceedingDB != null) {
							totalFields = totalFields + 1;
							totalproceeding++;
							if (!(proceedingDB.equals(proceeding))) {
								 System.out.println("***Wrong Proceeding");
								 wrongFields = wrongFields + 1;
								 indiWrong++;
								 wrongproceeding++;
							}
						}
						
						volumeDB = rs.getString("Volume");
						if (volumeDB != null) {
							totalFields = totalFields + 1;
							totalvolume++;
							if (!(volumeDB.equals(volume))) {
								 System.out.println("***Wrong Volume");
								 wrongFields = wrongFields + 1;
								 indiWrong++;
								 wrongvolume++;
							}
						}
						
						issueDB = rs.getString("Issue");
						if (issueDB != null) {
							totalFields = totalFields + 1;
							totalissue++;
							if (!(issueDB.equals(issue))) {
								 System.out.println("***Wrong Issue");
								 wrongFields = wrongFields + 1;
								 indiWrong++;
								 wrongissue++;
							}
						}
						
						numberDB = rs.getString("Number");
						if (numberDB != null) {
							totalFields = totalFields + 1;
							totalnumber++;
							if (!(numberDB.equals(number))) {
								 System.out.println("***Wrong Number");
								 wrongFields = wrongFields + 1;
								 indiWrong++;
								 wrongnumber++;
							}
						}
						
						articleDB = rs.getString("Article");
						if (articleDB != null) {
							totalFields = totalFields + 1;
							totalarticle++;
							if (!(articleDB.equals(article))) {
								 System.out.println("***Wrong Article");
								 wrongFields = wrongFields + 1;
								 indiWrong++;
								 wrongarticle++;
							}
						}
						
						pageDB = rs.getString("Page");
						if (pageDB != null) {
							totalFields = totalFields + 1;
							totalpage++;
							if (!(pageDB.equals(page))) {
								 System.out.println("***Wrong Page");
								 wrongFields = wrongFields + 1;
								 indiWrong++;
								 wrongpage++;
							}
						}
						
						monthDB = rs.getString("Month");
						if (monthDB != null) {
							totalFields = totalFields + 1;
							totalmonth++;
							if (!(monthDB.equals(month))) {
								 System.out.println("***Wrong Month");
								 wrongFields = wrongFields + 1;
								 indiWrong++;
								 wrongmonth++;
							}
						}
						
						thesisDB = rs.getString("Thesis");
						if (thesisDB != null) {
							totalFields = totalFields + 1;
							totalthesis++;
							if (!(thesisDB.equals(thesis))) {
								 System.out.println("***Wrong Thesis");
								 wrongFields = wrongFields + 1;
								 indiWrong++;
								 wrongthesis++;
							}
						}
						
						chapterDB = rs.getString("Chapter");
						if (chapterDB != null) {
							totalFields = totalFields + 1;
							totalchapter++;
							if (!(chapterDB.equals(chapter))) {
								 System.out.println("***Wrong Chapter");
								 wrongFields = wrongFields + 1;
								 indiWrong++;
								 wrongchapter++;
							}
						}
						
						editorsDB = rs.getString("Editors");
						if (editorsDB != null) {
							totalFields = totalFields + 1;
							totaleditor++;
							if (!(editorsDB.equals(editors))) {
								 System.out.println("***Wrong Editors");
								 wrongFields = wrongFields + 1;
								 indiWrong++;
								 wrongeditor++;
							}
						}
						
						publisherDB = rs.getString("Publisher");
						if (publisherDB != null) {
							totalFields = totalFields + 1;
							totalpublisher++;
							if (!(publisherDB.equals(publisher))) {
								 System.out.println("***Wrong Publisher");
								 wrongFields = wrongFields + 1;
								 indiWrong++;
								 wrongpublisher++;
							}
						}
						
						yearDB = rs.getInt("Year");
						if (yearDB != 0) {
							totalFields = totalFields + 1;
							totalyear++;
							if (year != null) {
								if (yearDB != Integer.parseInt(year)) {
									 System.out.println("***Wrong Year");
									 wrongFields = wrongFields + 1;
									 indiWrong++;
									 wrongyear++;
								}
							} else {
								System.out.println("***Wrong Year");
								wrongFields = wrongFields + 1;
								indiWrong++;
								wrongyear++;
							}
						}
						
						if (indiWrong==0) totalCorrect += count + ",";
						
					}
					

					System.out.println("===== remaining fields " + count + " =====");
					ca.printArray();
					
					System.out.println("===== end " + count + " =====\n\n");
					
					count++;
					data = "";
				}
				buffer = in.readLine();
			}
			
			in.close();
			rs.close();
			db.closeConnection(conn);

			
			System.out.println();
			System.out.println("===== overall statistics =====");
			System.out.println("Total Fields: " + totalFields);
			System.out.println("Correct Fields: " + (totalFields - wrongFields));
			System.out.println("Wrong Fields: " + wrongFields);
			System.out.println("Accuracy: " + (totalFields - wrongFields)/(float)totalFields);
			System.out.println("All correct: " + totalCorrect);
			System.out.println("\n===== fields statistics =====");
			System.out.println("Author: " + (totalauthor-wrongauthor) + "/" + totalauthor + ", accuracy: " + (double) (totalauthor-wrongauthor) / totalauthor);
			System.out.println("Title: " + (totaltitle-wrongtitle) + "/" + totaltitle + ", accuracy: " + (double) (totaltitle-wrongtitle) / totaltitle);
			System.out.println("Journal: " + (totaljournal-wrongjournal) + "/" + totaljournal + ", accuracy: " + (double) (totaljournal-wrongjournal) / totaljournal);
			System.out.println("Proceeding: " + (totalproceeding-wrongproceeding) + "/" + totalproceeding + ", accuracy: " + (double) (totalproceeding-wrongproceeding) / totalproceeding);
			System.out.println("Volume: " + (totalvolume-wrongvolume) + "/" + totalvolume + ", accuracy: " + (double) (totalvolume-wrongvolume) / totalvolume);
			System.out.println("Issue: " + (totalissue-wrongissue) + "/" + totalissue + ", accuracy: " + (double) (totalissue-wrongissue) / totalissue);
			System.out.println("Number: " + (totalnumber-wrongnumber) + "/" + totalnumber + ", accuracy: " + (double) (totalnumber-wrongnumber) / totalnumber);
			System.out.println("Article: " + (totalarticle-wrongarticle) + "/" + totalarticle + ", accuracy: " + (double) (totalarticle-wrongarticle) / totalarticle);
			System.out.println("Page: " + (totalpage-wrongpage) + "/" + totalpage + ", accuracy: " + (double) (totalpage-wrongpage) / totalpage);
			System.out.println("Month: " + (totalmonth-wrongmonth) + "/" + totalmonth + ", accuracy: " + (double) (totalmonth-wrongmonth) / totalmonth);
			System.out.println("Thesis: " + (totalthesis-wrongthesis) + "/" + totalthesis + ", accuracy: " + (double) (totalthesis-wrongthesis) / totalthesis);
			System.out.println("Chapter: " + (totalchapter-wrongchapter) + "/" + totalchapter + ", accuracy: " + (double) (totalchapter-wrongchapter) / totalchapter);
			System.out.println("Editor: " + (totaleditor-wrongeditor) + "/" + totaleditor + ", accuracy: " + (double) (totaleditor-wrongeditor) / totaleditor);
			System.out.println("Publisher: " + (totalpublisher-wrongpublisher) + "/" + totalpublisher + ", accuracy: " + (double) (totalpublisher-wrongpublisher) / totalpublisher);
			System.out.println("Year: " + (totalyear-wrongyear) + "/" + totalyear + ", accuracy: " + (double) (totalyear-wrongyear) / totalyear);
			
		} 
		catch (IOException e)
		{
			System.out.println("IOException!");
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			System.out.println("SQLException!");
			e.printStackTrace();
		}
		
    	Calendar cal2 = Calendar.getInstance();
    	cal2.getTime();
    	System.out.println();
    	System.out.println("End Time = " + sdf.format(cal2.getTime()));
    	System.out.println();
    	
    	long timeDifInMilliSec = cal2.getTimeInMillis() - cal.getTimeInMillis();
    	long timeDifSeconds = timeDifInMilliSec / (long) 1000;
    	System.out.println("Running Time = " + timeDifSeconds + " seconds");
    	
		
	}
}
