package main;

import java.lang.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.*;
import db.*;
import java.net.*;
import java.io.*;
import org.apache.commons.lang3.*;
import common.CommonFunction;
import weka.*;

public class CheckArray {
	String originalInput;
	private String[] array;
	
	private static HashMap<Integer, ArrayList> jmap = new HashMap<Integer, ArrayList>();
	private static HashMap<Integer, ArrayList> cmap = new HashMap<Integer, ArrayList>();
	private static HashMap<Integer, ArrayList> pmap = new HashMap<Integer, ArrayList>();
	
	private ProcessData process = new ProcessData();
	private TestWeka test = new TestWeka();
	
	public static void constructMap() {
		try {
			DBConnection db = new DBConnection();
			Connection conn = db.getConnection();
			String sql;
			ResultSet rs;
			ArrayList list = new ArrayList();
			int cnt;
			
			sql = "select * from journal";
			rs = db.getResultSet(conn, sql);
			cnt=1;
			while (rs.next()) {
				list = new ArrayList();
				list.add(rs.getString(1)==null?"":rs.getString(1));
				list.add(rs.getString(2)==null?"":rs.getString(2));
				list.add(rs.getString(3)==null?"":rs.getString(3));
				list.add(rs.getString(4)==null?"":rs.getString(4));
				jmap.put(cnt, list);
				cnt++;
			}

			sql = "select * from conference";
			rs = db.getResultSet(conn, sql);
			cnt=1;
			while (rs.next()) {
				list = new ArrayList();
				list.add(rs.getString(1)==null?"":rs.getString(1));
				list.add(rs.getString(2)==null?"":rs.getString(2));
				list.add(rs.getString(3)==null?"":rs.getString(3));
				cmap.put(cnt, list);
				cnt++;
			}

			sql = "select * from publisher";
			rs = db.getResultSet(conn, sql);
			cnt=1;
			while (rs.next()) {
				list = new ArrayList();
				list.add(rs.getString(1));
				list.add(rs.getString(2));
				pmap.put(cnt, list);
				cnt++;
			}
			
			conn.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String[] getArray() {
		return array;
	}
	
	public void processData() {
		test.buildTree();
	}
	
	/**
	 * CheckArray - constructor
	 */
	public CheckArray(String Input)
	{
		originalInput = Input;
	}
	
	/**
	 * splitByComma()
	 * - split the original input string (without title) by comma
	 */
	public void splitByComma()
	{
		String originalInputWithoutTitle = originalInput;
		
		if (originalInput.indexOf('\"') < originalInput.lastIndexOf('\"'))
		{
			originalInputWithoutTitle = originalInput.substring(0, originalInput.indexOf('\"')) + originalInput.substring(originalInput.lastIndexOf('\"')+1);
		}

		array = originalInputWithoutTitle.split(",");
		array = CommonFunction.trimArray(array);
	}
	
	/**
	 * getYearMonth()
	 * - search for the year, month field
	 * @return year, month extracted
	 */
	public String getYearMonth()
	{
		String monthNum = "";
		String yearMonth = "";
		String monthNumWords_1[] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
		String monthNumWords_2[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
		String monthWords[] = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
		
		Matcher yearMonthOnly_1;
		Matcher monthYearOnly_1;
		Matcher yearMonthOnly_2;
		Matcher monthYearOnly_2;
		int flag = 0;
		
		for (int i=0; i<array.length; i++)
		{
			yearMonthOnly_1 = Pattern.compile("\\d{4}(\\.)\\d{2}").matcher(array[i]);
			monthYearOnly_1 = Pattern.compile("\\d{2}(\\.)\\d{4}").matcher(array[i]);
			yearMonthOnly_2 = Pattern.compile("\\d{4}(\\.)\\d{1}").matcher(array[i]);
			monthYearOnly_2 = Pattern.compile("\\d{1}(\\.)\\d{4}").matcher(array[i]);
			
			// year.month (2010.06)
			if (yearMonthOnly_1.find())
			{
				yearMonth = yearMonthOnly_1.group();
				
				if (checkYear(yearMonth.substring(0, yearMonth.indexOf("."))))
				{
					monthNum = yearMonth.substring(yearMonth.indexOf(".") + 1);
					
					for (int j=0; j<12; j++) {
						if (monthNum.equals(monthNumWords_1[j]))
						{
							yearMonth = yearMonth.substring(0, yearMonth.indexOf(".")) + "," + monthWords[j].substring(0, 1).toUpperCase() + monthWords[j].substring(1);
							flag = 1;
							break;
						}
					}
				} else {
					yearMonth = "";
				}
			} else {
			// month.year (06.2010)
				if (monthYearOnly_1.find())
				{
					yearMonth = monthYearOnly_1.group();
					
					if (checkYear(yearMonth.substring(yearMonth.indexOf(".") + 1)))
					{
						monthNum = yearMonth.substring(0, yearMonth.indexOf("."));
						
						for (int j=0; j<12; j++) {
							if (monthNum.equals(monthNumWords_1[j]))
							{
								yearMonth = yearMonth.substring(yearMonth.indexOf(".") + 1) + "," + monthWords[j].substring(0, 1).toUpperCase() + monthWords[j].substring(1);
								flag = 1;
								break;
							}
						}
					} else {
						yearMonth = "";
					}
				} else {
					// year.month (2010.6)
					if (yearMonthOnly_2.find())
					{
						yearMonth = yearMonthOnly_2.group();
						
						if (checkYear(yearMonth.substring(0, yearMonth.indexOf("."))))
						{
							monthNum = yearMonth.substring(yearMonth.indexOf(".") + 1);
							
							for (int j=0; j<12; j++) {
								if (monthNum.equals(monthNumWords_2[j]))
								{
									yearMonth = yearMonth.substring(0, yearMonth.indexOf(".")) + "," + monthWords[j].substring(0, 1).toUpperCase() + monthWords[j].substring(1);
									flag = 1;
									break;
								}
							}
						} else {
							yearMonth = "";
						}
					} else {
					// month.year (6.2010)
						if (monthYearOnly_2.find())
						{
							yearMonth = monthYearOnly_2.group();
							
							if (checkYear(yearMonth.substring(yearMonth.indexOf(".") + 1)))
							{
								monthNum = yearMonth.substring(0, yearMonth.indexOf("."));
								
								for (int j=0; j<12; j++) {
									if (monthNum.equals(monthNumWords_2[j]))
									{
										yearMonth = yearMonth.substring(yearMonth.indexOf(".") + 1) + "," + monthWords[j].substring(0, 1).toUpperCase() + monthWords[j].substring(1);
										flag = 1;
										break;
									}
								}
							} else {
								yearMonth = "";
							}
						}
					}
				}
			}
			
			if (flag==1) break;
		}
		
		return yearMonth;
	}
	
	/**
	 * getMonth()
	 * - search for the month field
	 * @return month extracted
	 */
	public String getMonth()
	{
		String month = "";
		String monthWords[] = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
		int indexOfLong, indexOfShort;
		
		Matcher dateTodate;
		String dateString;
		String startMonth, endMonth;
		int flag = 0;
		
		
		for (int i=0; i<array.length; i++)
		{
			// month - month
			if (array[i].contains("-")) {
				
				startMonth = array[i].substring(0, array[i].indexOf("-"));
				endMonth = array[i].substring(array[i].indexOf("-")+1);
				
				for (int j=0; j<12; j++) {
					if ((startMonth.toLowerCase().contains(monthWords[j])) | (startMonth.toLowerCase().contains(monthWords[j].substring(0, 3))))
					{
						if (month != "") {
							month = month + "-" + monthWords[j].substring(0, 1).toUpperCase() + monthWords[j].substring(1);
						} else {
							month = month + monthWords[j].substring(0, 1).toUpperCase() + monthWords[j].substring(1);
						}
						break;
					}
				}
				
				for (int j=0; j<12; j++) {
					if ((endMonth.toLowerCase().contains(monthWords[j])) | (endMonth.toLowerCase().contains(monthWords[j].substring(0, 3))))
					{
						if (month != "") {
							month = month + "-" + monthWords[j].substring(0, 1).toUpperCase() + monthWords[j].substring(1);
						} else {
							month = month + monthWords[j].substring(0, 1).toUpperCase() + monthWords[j].substring(1);
						}
						break;
					}
				}
			} else {
				// month, month / month
				for (int j=0; j<12; j++) {
					indexOfShort = array[i].toLowerCase().indexOf(monthWords[j].substring(0, 3));
					
					if (indexOfShort >= 0)
					{
						if ((indexOfShort != 0) && (indexOfShort-2 > 0)) {
							if (!CommonFunction.isCharacter(array[i].substring(indexOfShort-2, indexOfShort-1))) {
								if (month != "") {
									month = month + "-" + monthWords[j].substring(0, 1).toUpperCase() + monthWords[j].substring(1);
								} else {
									month = month + monthWords[j].substring(0, 1).toUpperCase() + monthWords[j].substring(1);
								}
							}
						} else {
							if (month != "") {
								month = month + "-" + monthWords[j].substring(0, 1).toUpperCase() + monthWords[j].substring(1);
							} else {
								month = month + monthWords[j].substring(0, 1).toUpperCase() + monthWords[j].substring(1);
							}
						}
						break;
					}
				}
			}
		}

		if (month != "") {
			return month;
		} else {
			return null;
		}
	}
	

	/**
	 * getArticle()
	 * - search for the article field
	 * - with hints like article number/article no/article
	 * @return article extracted
	 */
	public String getArticle()
	{
		String article = null;
		String subString = null;
		Matcher numOnly;

		for (int i=0; i<array.length; i++)
		{
			// .*article number(\\.)*.*[0-9]+.*
			// article number / article number.
			if (array[i].toLowerCase().matches(".*article number( )*(\\.)*( )*[0-9]+.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("article number"))).trim();
				numOnly = Pattern.compile("\\d+").matcher(subString);

				if (numOnly.find())
				{
					article = numOnly.group();
					array[i] = CommonFunction.removePart(array[i], "article number", article);
					break;
				}
			}	

			// .*article no(\\.)*.*[0-9]+.*
			// article no / article no.
			if (array[i].toLowerCase().matches(".*article no( )*(\\.)*( )*[0-9]+.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("article no"))).trim();
				numOnly = Pattern.compile("\\d+").matcher(subString);

				if (numOnly.find())
				{
					article = numOnly.group();
					array[i] = CommonFunction.removePart(array[i], "article no", article);
					break;
				}
			}	

			// .*article(\\.)*.*[0-9]+.*
			// article / article.
			if (array[i].toLowerCase().matches(".*article( )*(\\.)*( )*[0-9]+.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("article"))).trim();
				numOnly = Pattern.compile("\\d+").matcher(subString);

				if (numOnly.find())
				{
					article = numOnly.group();
					array[i] = CommonFunction.removePart(array[i], "article", article);
					break;
				}
			}	
		}

		return article;
	}
	
	
	/**
	 * getVolume()
	 * - search for the volume field
	 * - with hints like volume/volume./vol/vol.
	 * @return volume extracted
	 */
	public String getVolume()
	{
		String volume = null;
		String subString = null;
		Matcher numOnly;

		for (int i=0; i<array.length; i++)
		{
			// .*volume(\\.)*.*[0-9]+.*
			// volume / volume.
			if (array[i].toLowerCase().matches(".*volume( )*(\\.)*( )*[0-9]+.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("volume"))).trim();
				numOnly = Pattern.compile("\\d+").matcher(subString);
				
				if (numOnly.find())
				{
					volume = numOnly.group();
					array[i] = CommonFunction.removePart(array[i], "volume", volume);
					break;
				}
			}
			
			// volume:
			if (array[i].toLowerCase().matches(".*volume( )*:( )*[0-9]+.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("volume"))).trim();
				numOnly = Pattern.compile("\\d+").matcher(subString);
				
				if (numOnly.find())
				{
					volume = numOnly.group();
					array[i] = CommonFunction.removePart(array[i], "volume", volume);
					break;
				}
			}

			// .*vol(\\.)*.*[0-9]+.*
			// vol / vol.
			if (array[i].toLowerCase().matches(".*vol( )*(\\.)*(:)*( )*[0-9]+.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("vol"))).trim();
				numOnly = Pattern.compile("\\d+").matcher(subString);
				
				if (numOnly.find())
				{
					volume = numOnly.group();
					array[i] = CommonFunction.removePart(array[i], "vol", volume);
					break;
					}
			}
			
			// vol:
			if (array[i].toLowerCase().matches(".*vol( )*:( )*[0-9]+.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("vol"))).trim();
				numOnly = Pattern.compile("\\d+").matcher(subString);
				
				if (numOnly.find())
				{
					volume = numOnly.group();
					array[i] = CommonFunction.removePart(array[i], "vol", volume);
					break;
					}
			}
			
			// 12()
			if (array[i].toLowerCase().matches(".*[0-9]+( )*\\(( )*\\).*"))
			{
				numOnly = Pattern.compile("[0-9]+").matcher(array[i]);
				
				if (numOnly.find())
				{
					volume = numOnly.group();
					break;
				}
			}
		}
		
		return volume;
	}
	
	/**
	 * getVolumeLast()
	 * - search for the volume field
	 * - with hints like volume/volume./vol/vol.
	 * @return volume extracted
	 */
	public String getVolumeLast()
	{
		String volume = null;
		String subString = null;

		for (int i=0; i<array.length; i++)
		{
			// Vol. E94-A
			// vol. IT-56
			subString = array[i].trim();
			
			if (subString.toLowerCase().contains("vol")) {
				volume = subString.substring(subString.indexOf(" ")+1);
				break;
			}
		}

		return volume;
	}
	
	/**
	 * getVolumeLast2_NumberOnly()
	 * - search for the volume field
	 * @return volume extracted
	 */
	public String getVolumeLast2_NumberOnly()
	{
		String volume = null;
		Matcher volNum;

		for (int i=0; i<array.length; i++)
		{
			// (1)
			volNum = Pattern.compile("[0-9]+").matcher(array[i]);
			if (volNum.find())
			{
				volume = volNum.group();
				array[i] = CommonFunction.removePart(array[i], volume, volume);
				break;
			}
		}

		return volume;
	}
	
	/**
	 * getNumberLast()
	 * - search for the number field
	 * @return number extracted
	 */
	public String getNumberLast()
	{
		String number = null;
		Matcher numberNum;

		for (int i=0; i<array.length; i++)
		{
			// e1001046
			numberNum = Pattern.compile("[0-9]+").matcher(array[i]);
			if (numberNum.find())
			{
				number = array[i];
				break;
			}
		}

		return number;
	}

	/**
	 * getIssue()
	 * - search for the issue field
	 * - with hints like issue/issue./iss/iss.
	 * @return issue extracted
	 */
	public String getIssue()
	{
		String issue = null;
		String subString = null;
		Matcher numOnly;

		for (int i=0; i<array.length; i++)
		{
			// .*issue(\\.)*.*[0-9]+.*
			// issue / issue.
			if (array[i].toLowerCase().matches(".*issue( )*(\\.)*( )*[0-9]+.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("issue"))).trim();
				numOnly = Pattern.compile("\\d+").matcher(subString);
				
				if (numOnly.find())
				{
					issue = numOnly.group();
					array[i] = CommonFunction.removePart(array[i], "issue", issue);
					break;
				}
			}
			
			// issue:
			if (array[i].toLowerCase().matches(".*issue( )*:( )*[0-9]+.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("issue"))).trim();
				numOnly = Pattern.compile("\\d+").matcher(subString);
				
				if (numOnly.find())
				{
					issue = numOnly.group();
					array[i] = CommonFunction.removePart(array[i], "issue", issue);
					break;
				}
			}
			
			// .*iss(\\.)*.*[0-9]+.*"
			// iss / iss.
			if (array[i].toLowerCase().matches(".*iss( )*(\\.)*( )*[0-9]+.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("iss"))).trim();
				numOnly = Pattern.compile("\\d+").matcher(subString);
				
				if (numOnly.find())
				{
					issue = numOnly.group();
					array[i] = CommonFunction.removePart(array[i], "iss", issue);
					break;
				}
			}	
			
			// iss:
			if (array[i].toLowerCase().matches(".*iss( )*:( )*[0-9]+.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("iss"))).trim();
				numOnly = Pattern.compile("\\d+").matcher(subString);
				
				if (numOnly.find())
				{
					issue = numOnly.group();
					array[i] = CommonFunction.removePart(array[i], "iss", issue);
					break;
				}
			}	
		}	
		
		return issue;
	}

	/**
	 * getNumber()
	 * - search for the number field
	 * - with hints like no/no./number/number.
	 * @return number extracted
	 */
	public String getNumber()
	{
		String number = null;
		String subString = null;
		Matcher numOnly;

		for (int i=0; i<array.length; i++)
		{
			// .*no(\\.)*.*[0-9]+.*
			// no / no.
			if (array[i].toLowerCase().matches(".*no( )*(\\.)*( )*[0-9]+.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("no"))).trim();
				numOnly = Pattern.compile("\\d+").matcher(subString);
				
				if (numOnly.find())
				{
					number = numOnly.group();
					array[i] = CommonFunction.removePart(array[i], "no", number);
					break;
				}
			}
			
			// no:
			if (array[i].toLowerCase().matches(".*no( )*:( )*[0-9]+.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("no"))).trim();
				numOnly = Pattern.compile("\\d+").matcher(subString);
				
				if (numOnly.find())
				{
					number = numOnly.group();
					array[i] = CommonFunction.removePart(array[i], "no", number);
					break;
				}
			}

			// .*number(\\.)*.*[0-9]+.*
			// number / number. / numbers 4-5
			if (array[i].toLowerCase().matches(".*number(s)?( )*(\\.)*( )*[0-9]+( )*([-]*( )*[0-9]+)*.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("number"))).trim();
				numOnly = Pattern.compile("[0-9]+( )*([-]*( )*[0-9]+)*").matcher(subString);
				
				if (numOnly.find())
				{
					number = numOnly.group();
					array[i] = CommonFunction.removePart(array[i], "number", number);
					break;
				}
			}	
			
			// number: / numbers: 4-5
			if (array[i].toLowerCase().matches(".*number(s)?( )*:( )*[0-9]+( )*([-]*( )*[0-9]+)*.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("number"))).trim();
				numOnly = Pattern.compile("[0-9]+( )*([-]*( )*[0-9]+)*").matcher(subString);
				
				if (numOnly.find())
				{
					number = numOnly.group();
					array[i] = CommonFunction.removePart(array[i], "number", number);
					break;
				}
			}
		}
		
		return number;
	}
	

	/**
	 * getPage()
	 * - search for the page field
	 * - with hints like page./pages./p
	 * @return page number extracted
	 */
	public String getPage()
	{
		String page = null;
		String subString = null;
		String pageSubString = null;
		Matcher numToNum;

		for (int i=0; i<array.length; i++)
		{
			// pages / pages. (p. 29:1-29:19)
			if (array[i].toLowerCase().matches(".*pages( )*(\\.)*( )*[0-9]+( )*:( )*[0-9]+( )*-( )*[0-9]+( )*:( )*[0-9]+.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("pages"))).trim();
				numToNum = Pattern.compile("[0-9]+( )*:( )*[0-9]+( )*-( )*[0-9]+( )*:( )*[0-9]+").matcher(subString);

				if (numToNum.find())
				{
					page = numToNum.group();
					array[i] = CommonFunction.removePart(array[i], "pages", page);
					break;
				}
			}
			
			// page / page. (p. 29:1-29:19)
			if (array[i].toLowerCase().matches(".*page( )*(\\.)*( )*[0-9]+( )*:( )*[0-9]+( )*-( )*[0-9]+( )*:( )*[0-9]+.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("page"))).trim();
				numToNum = Pattern.compile("[0-9]+( )*:( )*[0-9]+( )*-( )*[0-9]+( )*:( )*[0-9]+").matcher(subString);

				if (numToNum.find())
				{
					page = numToNum.group();					
					array[i] = CommonFunction.removePart(array[i], "page", page);
					break;
				}
			}

			// pp / pp. (p. 29:1-29:19)
			if (array[i].toLowerCase().matches(".*pp( )*(\\.)*( )*[0-9]+( )*:( )*[0-9]+( )*-( )*[0-9]+( )*:( )*[0-9]+.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("pp"))).trim();
				numToNum = Pattern.compile("[0-9]+( )*:( )*[0-9]+( )*-( )*[0-9]+( )*:( )*[0-9]+").matcher(subString);

				if (numToNum.find())
				{
					page = numToNum.group();
					array[i] = CommonFunction.removePart(array[i], "pp", page);
					break;
				}
			}

			// p / p. (p. 29:1-29:19)
			if (array[i].toLowerCase().matches(".*p( )*(\\.)*( )*[0-9]+( )*:( )*[0-9]+( )*-( )*[0-9]+( )*:( )*[0-9]+.*"))
			{
				subString = (array[i].substring(array[i].toLowerCase().indexOf("p"))).trim();
				numToNum = Pattern.compile("[0-9]+( )*:( )*[0-9]+( )*-( )*[0-9]+( )*:( )*[0-9]+").matcher(subString);

				if (numToNum.find())
				{
					page = numToNum.group();
					array[i] = CommonFunction.removePart(array[i], "p", page);
					break;
				}
			}
			
			
			if (page == null) {
				// .*pages(\\.)*.*[0-9]+( )*-( )*[0-9]+.*
				// pages / pages.
				if (array[i].toLowerCase().matches(".*pages( )*(\\.)*( )*[0-9]+( )*-( )*[0-9]+.*"))
				{
					subString = (array[i].substring(array[i].toLowerCase().indexOf("pages"))).trim();
					numToNum = Pattern.compile("[0-9]+( )*-( )*[0-9]+").matcher(subString);
	
					if (numToNum.find())
					{
						page = numToNum.group();
						array[i] = CommonFunction.removePart(array[i], "pages", page);
						break;
					}
				}
	
				// .*page(\\.)*.*[0-9]+( )*-( )*[0-9]+.*
				// page / page.
				if (array[i].toLowerCase().matches(".*page( )*(\\.)*( )*[0-9]+( )*-( )*[0-9]+.*"))
				{
					subString = (array[i].substring(array[i].toLowerCase().indexOf("page"))).trim();
					numToNum = Pattern.compile("[0-9]+( )*-( )*[0-9]+").matcher(subString);
	
					if (numToNum.find())
					{
						page = numToNum.group();					
						array[i] = CommonFunction.removePart(array[i], "page", page);
						break;
					}
				}
	
				// .*pp(\\.)*.*[0-9]+( )*-( )*[0-9]+.*
				// pp / pp.
				if (array[i].toLowerCase().matches(".*pp( )*(\\.)*( )*[0-9]+( )*-( )*[0-9]+.*"))
				{
					subString = (array[i].substring(array[i].toLowerCase().indexOf("pp"))).trim();
					numToNum = Pattern.compile("[0-9]+( )*-( )*[0-9]+").matcher(subString);
	
					if (numToNum.find())
					{
						page = numToNum.group();
						array[i] = CommonFunction.removePart(array[i], "pp", page);
						break;
					}
				}
	
				// .*p(\\.)*.*[0-9]+( )*-( )*[0-9]+.*
				// p / p.
				if (array[i].toLowerCase().matches(".*p( )*(\\.)*( )*[0-9]+( )*-( )*[0-9]+.*"))
				{
					subString = (array[i].substring(array[i].toLowerCase().indexOf("p"))).trim();
					numToNum = Pattern.compile("[0-9]+( )*-( )*[0-9]+").matcher(subString);
	
					if (numToNum.find())
					{
						page = numToNum.group();
						array[i] = CommonFunction.removePart(array[i], "p", page);
						break;
					}
				}
	
				// page number only
				if (array[i].toLowerCase().matches(".*[0-9]+( )*-( )*[0-9]+.*"))
				{
					numToNum = Pattern.compile("[0-9]+( )*-( )*[0-9]+").matcher(array[i]);
	
					if (numToNum.find())
					{
						page = numToNum.group();
						//array[i] = array[i].substring(0, array[i].indexOf(page)) + array[i].substring(array[i].indexOf(page) + page.length());
						array[i] = CommonFunction.removePart(array[i], page, page);
						break;
					}
				}
			}
			
			
		}

		if (page != null) {
			page = page.replaceAll(" ", "");
		}
		
		return page;
	}
	
	
	public String getYearLast(String inputTitle)
	{
		String year = null;
		Matcher yearOnly;
		
		// 2007-2008
		String startYear = null;
		String endYear = null;
		yearOnly = Pattern.compile("[0-9]{4}( )*-( )*[0-9]{4}").matcher(inputTitle);
		if (yearOnly.find())
		{
			year = yearOnly.group();
			year = year.replaceAll(" ", "");
			startYear = year.substring(0, year.indexOf("-"));
			endYear =  year.substring(year.indexOf("-")+1);
			
			if ((checkYear(startYear)) && checkYear(endYear))
			{
				return year;
			}
		}
		
		// 2007
		yearOnly = Pattern.compile("[0-9]{4}").matcher(inputTitle);
		if (yearOnly.find())
		{
			year = yearOnly.group();
			if (checkYear(year))
			{
				return year;
			}
		}
		
		for (int i=0; i<array.length; i++)
		{
			// 2007-2008
			yearOnly = Pattern.compile("[0-9]{4}( )*-( )*[0-9]{4}").matcher(array[i]);
			if (yearOnly.find())
			{
				year = yearOnly.group();
				year = year.replaceAll(" ", "");
				startYear = year.substring(0, year.indexOf("-"));
				endYear =  year.substring(year.indexOf("-")+1);
				
				if ((checkYear(startYear)) && checkYear(endYear))
				{
					return year;
				}
			}
			
			// 2007
			yearOnly = Pattern.compile("[0-9]{4}").matcher(array[i]);
			if (yearOnly.find())
			{
				year = yearOnly.group();
				if (checkYear(year))
				{
					return year;
				}
			}
			
		}
		
		return null;
	}
	

	/**
	 * getYear()
	 * - search for integer of 4 digits
	 * @return year extracted
	 */
	public String getYear()
	{
		String year = null;
		Matcher yearOnly;

		for (int i=0; i<array.length; i++)
		{
			// 2007
			yearOnly = Pattern.compile("[0-9]{4}").matcher(array[i]);
			if (yearOnly.find())
			{
				year = yearOnly.group();
				if (checkYear(year))
				{
					array[i] = CommonFunction.removePart(array[i], year, year);
					return year;
				}
			}
		}
		
		return null;
	}
	
	
	/**
	 *  checkYear(String year)
	 *  - check if the year found is between 1900 and system current year
	 *  @return true if year is valid
	 */
	private boolean checkYear(String year)
	{
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int checkYear = Integer.parseInt(year);
		
		if ((1900 <= checkYear) && (checkYear <= currentYear)) return true;
		else return false;
	}
	
	/**
	 * getVolumeIssue()
	 * - search for combined field of volume and issue (eg. 12(3))
	 * @return extract volume and issue, return in format 'volume,issue'
	 */
	public String getVolumeIssue()
	{
		String volIss = null;
		
		Matcher volIssuePage;
		
		Matcher volIssueBracket;
		Matcher volIssueSemiColon;
		
		Matcher fourdigit;
		
		String volume = null;
		String issue = null;
		String page = null;
		
		for (int i=0; i<array.length; i++)
		{
			// vol(iss):page
			// 12(2-5):3-6
			volIssuePage = Pattern.compile("[0-9]+( )*\\([0-9]+([-/]*[0-9]+)*\\):[0-9]+[-/]*[0-9]+").matcher(array[i]);
			if (volIssuePage.find())
			{
				volIss = volIssuePage.group();
				
				volume = volIss.substring(0, volIss.indexOf("("));
				issue = volIss.substring(volIss.indexOf("(")+1, volIss.indexOf(")"));
				page = volIss.substring(volIss.indexOf(":")+1);
				
				array[i] = CommonFunction.removePart(array[i], volIss, volIss);
				volIss = volume + "," + issue + "," + page;

				break;
			}

			// 12(2-5) 12(2)
			volIssueBracket = Pattern.compile("[0-9]+( )*\\([0-9]+([-/]*[0-9]+)*\\)").matcher(array[i]);
			if (volIssueBracket.find())
			{
				volIss = volIssueBracket.group();
				
				volume = volIss.substring(0, volIss.indexOf("("));
				issue = volIss.substring(volIss.indexOf("(")+1, volIss.indexOf(")"));
				
				// issue seems impossible larger than 4 digits
				fourdigit = Pattern.compile("[0-9]{4}").matcher(issue);
				if (fourdigit.find())
				{
					array[i] = CommonFunction.removePart(array[i], volIss, volIss);
					volIss = "y" + volume + "," + issue;
				} else {
					array[i] = CommonFunction.removePart(array[i], volIss, volIss);
					volIss = volume + "," + issue;
				}
				
				break;
			}
			
			// 12:2-5 12:2
			volIssueSemiColon = Pattern.compile("[0-9]+:[0-9]+[-/[0-9]*]*(^:)").matcher(array[i]);
			if (volIssueSemiColon.find())
			{
				volIss = volIssueSemiColon.group();
				
				volume = volIss.substring(0, volIss.indexOf(":"));
				issue = volIss.substring(volIss.indexOf(":")+1);
				
				array[i] = CommonFunction.removePart(array[i], volIss, volIss);
				volIss = volume + "," + issue;
				
				break;
			}
			
		}
		
		return volIss;
	}
	
	/**
	 * getJournal()
	 * - search for the journal name
	 * @return journal name
	 */
	public String getJournal()
	{
		String journal = null;
		String[] checkJournal;
		String checkStr;
		ArrayList list = new ArrayList();
		String fullname, short1, short2;
		
		try
		{			
			for (int i=0; i<array.length; i++)
			{
				if (array[i].toLowerCase().indexOf("journal of") >= 0)
				{
					journal = array[i].substring(array[i].toLowerCase().indexOf("journal of")).trim();
					//System.out.println("Journal: " + journal);
					
					array[i] = "";
					break;
				}
				
				
				if (journal==null) {
					if (array[i].toLowerCase().indexOf("journal") >= 0)
					{
						journal = array[i].trim();
						//System.out.println("Journal: " + journal);
						array[i] = "";
						break;
					}
				}

				if (journal==null) {
					checkJournal = array[i].split(" ");
					checkStr = "";
					for (int j=0; j<checkJournal.length; j++) {
						if (checkJournal.length>1)
						{
							if (j<1) checkStr = checkJournal[j] + " " + checkJournal[j+1];
							else if (j==checkJournal.length-1) checkStr = checkJournal[j-1] + " " + checkJournal[j];
							else checkStr = checkJournal[j-1] + " " + checkJournal[j] + " " + checkJournal[j+1];
						}
						else
						{
							checkStr = checkJournal[0];
						}
						checkStr = checkStr.replaceAll("'", "\\\\'").trim();
						
						if (!checkStr.equals("")) {
							for (int k=1; k<=jmap.size(); k++) {
								list = jmap.get(k);
								fullname = list.get(1).toString();
								short1 = list.get(2).toString();
								short2 = list.get(3).toString();
								
								// check exact match of journal name
								if (fullname.toLowerCase().indexOf(checkStr.toLowerCase()) >= 0) {
									if (array[i].toLowerCase().indexOf(fullname.toLowerCase()) >= 0) {
										journal = array[i].substring(array[i].toLowerCase().indexOf(fullname.toLowerCase()));
										
										if (array[i].toLowerCase().indexOf(fullname.toLowerCase()) != 0) {
											array[i] = array[i].substring(0, array[i].toLowerCase().indexOf(fullname.toLowerCase())-1);
										} else {
											array[i] = "";
										}
										
										break;
									}
								}
								
								// check partial match of journal name
								//if (fullname.toLowerCase().indexOf(checkStr.toLowerCase()) >= 0) {
								/*if (checkStr.toLowerCase().indexOf(fullname.toLowerCase()) >= 0) {
									journal = fullname;
									break;
								}
								//if (!short1.equals("") && short1.toLowerCase().indexOf(checkStr.toLowerCase()) >= 0) {
								if (!short1.equals("") && checkStr.toLowerCase().indexOf(short1.toLowerCase()) >= 0) {
									journal = fullname;
									break;
								}
								//if (!short2.equals("") && short2.toLowerCase().indexOf(checkStr.toLowerCase()) >= 0) {
								if (!short2.equals("") && checkStr.toLowerCase().indexOf(short2.toLowerCase()) >= 0) {
									journal = fullname;
									break;
								}*/
							}
						}
						
						if (journal!=null) break;
					}
				}

			}
		}
		catch (Exception e)
		{
			System.out.println("[CheckArray.getJournal()] Exception");
			e.printStackTrace();
		}
		
		
		if (journal!=null) {
			if (journal.toLowerCase().indexOf("the") == 0)
			{
				journal = journal.substring(3);
			}
		}
		
		return journal;
	}
	

	/**
	 * getJournal_Last_fromTitle()
	 * - search for the journal name
	 * @return journal name
	 */
	public String getJournal_Last_fromTitle(String inputTitle)
	{
		String journal = null;
		DBConnection db = new DBConnection();
		Connection conn = db.getConnection();
		ResultSet rs = null;
		String sql = "";
		String[] checkJournal;
		String checkStr;
		
		ArrayList list = new ArrayList();
		String fullname, short1, short2;
		
		try
		{			
			checkJournal = inputTitle.split(" ");
			checkStr = "";
			for (int j=0; j<checkJournal.length; j++) {
				if (checkJournal.length>1)
				{
					if (j<1) checkStr = checkJournal[j] + " " + checkJournal[j+1];
					else if (j==checkJournal.length-1) checkStr = checkJournal[j-1] + " " + checkJournal[j];
					else checkStr = checkJournal[j-1] + " " + checkJournal[j] + " " + checkJournal[j+1];
				}
				else
				{
					checkStr = checkJournal[0];
				}
				checkStr = checkStr.replaceAll("'", "\\\\'").trim();
				checkStr = checkStr.replaceAll("\\.", "").trim();
				
				if (!checkStr.equals("")) {
					for (int k=1; k<=jmap.size(); k++) {
						list = jmap.get(k);
						fullname = list.get(1).toString();
						short1 = list.get(2).toString();
						short2 = list.get(3).toString();
						
						// check exact match of journal name
						if (fullname.toLowerCase().indexOf(checkStr.toLowerCase()) >= 0) {
							if (inputTitle.toLowerCase().indexOf(fullname.toLowerCase()) >= 0) {
								journal = fullname;
								break;
							}
						}
						
						// check partial match of journal name
						//if (fullname.toLowerCase().indexOf(checkStr.toLowerCase()) >= 0) {
						/*if (checkStr.toLowerCase().indexOf(fullname.toLowerCase()) >= 0) {
							journal = fullname;
							break;
						}
						//if (!short1.equals("") && short1.toLowerCase().indexOf(checkStr.toLowerCase()) >= 0) {
						if (!short1.equals("") && checkStr.toLowerCase().indexOf(short1.toLowerCase()) >= 0) {
							journal = fullname;
							break;
						}
						//if (!short2.equals("") && short2.toLowerCase().indexOf(checkStr.toLowerCase()) >= 0) {
						if (!short2.equals("") && checkStr.toLowerCase().indexOf(short2.toLowerCase()) >= 0) {
							journal = fullname;
							break;
						}*/
					}
				}
				
				if (journal!=null) break;
			}
		}
		catch (Exception e)
		{
			System.out.println("[CheckArray.getJournal()] Exception");
			e.printStackTrace();
		}
		
		
		if (journal!=null) {
			if (journal.toLowerCase().indexOf("the") == 0)
			{
				journal = journal.substring(3);
			}
		}
		
		return journal;
	}

	

	/**
	 * getProceeding()
	 * - search for the proceeding name
	 * @return proceeding name
	 */
	public String getProceeding()
	{
		String proceeding = null;
		String[] checkProc;
		String checkStr;
		ArrayList list = new ArrayList();
		String fullname, shortname;
		
		try
		{		
			for (int i=0; i<array.length; i++)
			{
				if (proceeding==null)
				{
					if (array[i].toLowerCase().indexOf("in proceedings of the") >= 0)
					{
						proceeding = (array[i].substring(array[i].toLowerCase().indexOf("in proceedings of the") + 21)).trim();
						//System.out.println("proceedings: " + proceeding);
						
						array[i] = array[i].substring(0, array[i].toLowerCase().indexOf("in proceedings of the"));
						break;
					}
					
					if (array[i].toLowerCase().indexOf("in proceedings of") >= 0)
					{
						proceeding = (array[i].substring(array[i].toLowerCase().indexOf("in proceedings of") + 17)).trim();
						//System.out.println("proceedings: " + proceeding);
						
						array[i] = array[i].substring(0, array[i].toLowerCase().indexOf("in proceedings of"));
						break;
					}
					
					if (array[i].toLowerCase().indexOf("proceedings of the") >= 0)
					{
						proceeding = (array[i].substring(array[i].toLowerCase().indexOf("proceedings of the") + 18)).trim();
						//System.out.println("proceedings: " + proceeding);
						
						array[i] = array[i].substring(0, array[i].toLowerCase().indexOf("proceedings of the"));
						break;
					}
					
					if (array[i].toLowerCase().indexOf("proceedings of") >= 0)
					{
						proceeding = (array[i].substring(array[i].toLowerCase().indexOf("proceedings of") + 14)).trim();
						//System.out.println("proceedings: " + proceeding);
						
						array[i] = array[i].substring(0, array[i].toLowerCase().indexOf("proceedings of"));
						break;
					}
					
					if (array[i].toLowerCase().indexOf("proceedings") >= 0)
					{
						proceeding = array[i];
						
						if (!CommonFunction.isCharacter(proceeding.substring(0,1))) {
							proceeding = proceeding.substring(1);
						}
						
						if (!CommonFunction.isCharacter(proceeding.substring(proceeding.length()-1))) {
							proceeding = proceeding.substring(0, proceeding.length()-1);
						}
						
						array[i] = "";
						break;
					}
					
					if (array[i].toLowerCase().indexOf("conference") >= 0)
					{
						proceeding = array[i];
						
						array[i] = "";
						break;
					}
				}
				
				checkProc = array[i].split(" ");
				for (int j=0; j<checkProc.length; j++) {
					if (checkProc.length < 6) {
						checkStr = array[i];
					} else {
						if (j < 2 || j > checkProc.length-3) 
							continue;
						else 
							checkStr = checkProc[j-2] + " " + checkProc[j-1] + " " + checkProc[j] + " " + checkProc[j+1] + " " + checkProc[j+2];
					}
					checkStr = checkStr.replaceAll("'", "\\\\'");
					
					if (!checkStr.equals("")) {
						for (int k=1; k<=cmap.size(); k++) {
							list = cmap.get(k);
							fullname = list.get(1).toString();
							shortname = list.get(2).toString();

							// check exact match of proceeding name
							if (fullname.toLowerCase().indexOf(checkStr) >= 0) {
								if (array[i].toLowerCase().indexOf(fullname.toLowerCase()) >= 0) {

									if (array[i].toLowerCase().indexOf(fullname.toLowerCase()) != 0) {
										array[i] = array[i].substring(0, array[i].toLowerCase().indexOf(fullname.toLowerCase())-1);
									} else {
										array[i] = "";
									}
									
									break;
								}
							}
							
							// check partial match of proceeding name
							/*if (fullname.toLowerCase().indexOf(checkStr.toLowerCase()) >= 0) {
								proceeding = fullname;
								break;
							}
							if (!shortname.equals("") && shortname.toLowerCase().indexOf(checkStr.toLowerCase()) >= 0) {
								proceeding = fullname;
								break;
							}*/
						}
					}
					
					/*if (!checkStr.equals("")) {
						sql = "select * from conference where fullname like '%" + URLEncoder.encode(checkStr, "UTF8") + "%'";
						rs = db.getResultSet(conn, sql);
						while (rs.next()) {
							if (array[i].toLowerCase().contains(rs.getString(2).toLowerCase())) {
								// comment block
								proceeding = rs.getString(2);	
								if (rs.getString(3)!=null && array[i].contains(rs.getString(3))) {
									proceeding += " (" + rs.getString(3) + ")";
									array[i] = CommonFunction.removePart(array[i], rs.getString(2), rs.getString(2));
									break;
								}
								array[i] = CommonFunction.removePart(array[i], proceeding, proceeding);
								// comment block
								
								// if check similar --> use whole string (rather than use db string)
								proceeding = array[i].trim();
								array[i] = "";
								break;		
							}
						}						
					}*/
					
					if (proceeding!=null) break;
				}

				if (proceeding!=null) break;
			}

		}
		catch (Exception e)
		{
			System.out.println("[CheckArray.getJournal()] Exception");
			e.printStackTrace();
		}
		/*catch (UnsupportedEncodingException e)
		{
			System.out.println("[CheckArray.getJournal()] UnsupportedEncodingException");
			e.printStackTrace();
		}*/
		

		if (proceeding!=null) {
			
			if (proceeding.toLowerCase().indexOf("the") == 0)
			{
				proceeding = proceeding.substring(3);
			}
			
			if (proceeding.toLowerCase().indexOf("published in the") != -1) {
				proceeding = proceeding.substring(proceeding.toLowerCase().indexOf("published in the")+16);
			}
			
			if (proceeding.toLowerCase().indexOf("presented in the ") != -1) {
				proceeding = proceeding.substring(proceeding.toLowerCase().indexOf("presented in the")+16);
			}
		}
		
		return proceeding;
	}
	
	

	/**
	 * getEditors()
	 * - search for the editors
	 * @return editors
	 */
	public String getEditors()
	{
		String editors = null;
		
		for (int i=0; i<array.length; i++)
		{
			if (array[i].toLowerCase().indexOf("eds") >= 0)
			{
				editors = array[i].trim();
				
				array[i] = "";
				break;
			}
		}

		return editors;
	}
	
	/**
	 * getPublisher()
	 * - search for the publisher
	 * @return publisher
	 */
	public String getPublisher()
	{
		String publisher = null;
		DBConnection db = new DBConnection();
		Connection conn = db.getConnection();
		ResultSet rs = null;
		String sql = "";
		String[] checkPublisher;
		String checkStr;
		ArrayList list = new ArrayList();
		
		try
		{		
			for (int i=0; i<array.length; i++)
			{
				checkPublisher = array[i].split(" ");
				for (int j=0; j<checkPublisher.length; j++) {
					if (checkPublisher.length>1)
					{
						if (j<1) checkStr = checkPublisher[j] + " " + checkPublisher[j+1];
						else if (j==checkPublisher.length-1) checkStr = checkPublisher[checkPublisher.length-2] + " " + checkPublisher[checkPublisher.length-1];
						else checkStr = checkPublisher[j-1] + " " + checkPublisher[j] + " " + checkPublisher[j+1];
					}
					else
					{
						checkStr = checkPublisher[j];
					}
					checkStr = checkStr.replaceAll("'", "\\\\'");
					
					if (!checkStr.equals("")) {
						for (int k=1; k<=pmap.size(); k++) {
							list = pmap.get(k);
							if (list.get(1).toString().toLowerCase().indexOf(checkStr) >=0) {
								if (array[i].toLowerCase().indexOf(list.get(1).toString().toLowerCase()) >=0) {
									publisher = list.get(1).toString();
								}
							}
						}
					}

					/*if (!checkStr.equals("")) {
						//sql = "select * from publisher where pubname like '%" + URLEncoder.encode(checkStr, "UTF8") + "%'";
						sql = "select * from publisher where pubname like '%" + checkStr + "%'";
						rs = db.getResultSet(conn, sql);
						while (rs.next()) {
							if (array[i].toLowerCase().contains(rs.getString(2).toLowerCase())) {
								// if check similar --> use whole string (rather than use db string)
								
								publisher = rs.getString(2);
								array[i] = CommonFunction.removePart(array[i], publisher, publisher);
								
								//publisher = array[i].trim();
								//array[i] = "";
								
								break;		
							}
						}						
					}*/
					
					if (publisher != null) break;
				}
				
				if (publisher != null) break;
				
				if (publisher==null)
				{
					if (array[i].toLowerCase().indexOf("publication") >= 0)
					{
						publisher = array[i].trim();
						// substring from ":"
						if (publisher.indexOf(":") >= 0) {
							publisher = publisher.substring(publisher.indexOf(":")+1);
						}
						
						array[i] = "";
						break;
					}
					
					if (array[i].toLowerCase().indexOf("publishing") >= 0)
					{
						publisher = array[i].trim();
						// substring from ":"
						if (publisher.indexOf(":") >= 0) {
							publisher = publisher.substring(publisher.indexOf(":")+1);
						}
						
						array[i] = "";
						break;
					}
					
					if (array[i].toLowerCase().indexOf("publisher") >= 0)
					{
						publisher = array[i].trim();
						// substring from ":"
						if (publisher.indexOf(":") >= 0) {
							publisher = publisher.substring(publisher.indexOf(":")+1);
						}
						
						array[i] = "";
						break;
					}
					
					if (array[i].toLowerCase().indexOf("press") >= 0)
					{
						publisher = array[i].trim();
						// substring from ":"
						if (publisher.indexOf(":") >= 0) {
							publisher = publisher.substring(publisher.indexOf(":")+1);
						}
						
						array[i] = "";
						break;
					}
					
					if (array[i].toLowerCase().indexOf("ltd") >= 0)
					{
						publisher = array[i].trim();
						// substring from ":"
						if (publisher.indexOf(":") >= 0) {
							publisher = publisher.substring(publisher.indexOf(":")+1);
						}
						
						array[i] = "";
						break;
					}
				}
				
			}

			if (rs!=null) rs.close();
		}
		catch (SQLException e)
		{
			System.out.println("[CheckArray.getPublisher()] SQLException");
			e.printStackTrace();
		}
		/*catch (UnsupportedEncodingException e)
		{
			System.out.println("[CheckArray.getPublisher()] UnsupportedEncodingException");
			e.printStackTrace();
		}*/
		finally
		{
			db.closeConnection(conn);
		}
		
		// truncate "the" and "."
		if (publisher != null) {
			if (publisher.toLowerCase().indexOf("the") == 0)
			{
				publisher = publisher.substring(3);
			}
			
			
			if (publisher.toLowerCase().indexOf(".") == publisher.length()-1)
			{
				publisher = publisher.substring(0, publisher.length()-1);
			}
		}
		
		
		return publisher;
	}
	

	/**
	 * getThesis()
	 * - search for the thesis
	 * @return thesis
	 */
	public String getThesis()
	{
		String thesis = null;
		
		for (int i=0; i<array.length; i++)
		{
			if (array[i].toLowerCase().indexOf("thesis") >= 0)
			{
				thesis = array[i].trim();
				
				array[i] = "";
				break;
			}
		}

		return thesis;
	}
	
	
	
	/**
	 * getTitlePreSplit()
	 * - search for the title before splitting
	 * @return title
	 */
	public String getTitlePreSplit()
	{
		String title = null;

		if (originalInput.indexOf('\"') < originalInput.lastIndexOf('\"'))
		{
			title = (originalInput.substring(originalInput.indexOf('\"')+1, originalInput.lastIndexOf('\"'))).trim();
		}

		return title;
	}
	
	/**
	 * getTitlePostSplit()
	 * - search for the title after splitting
	 * @return title
	 */
	public String getTitlePostSplit()
	{
		String title = null;
		String str;
		Matcher match;
		String[] chunk;
		
		for (int i=0; i<array.length; i++)
		{			
			if (array[i].length() > 20)
			{
				match = Pattern.compile("[,\\.:].*[\\.,]").matcher(array[i]);
				if (match.find())
				{
					title = match.group().substring(1);
					array[i] = CommonFunction.removePart(array[i], title, title);
					if (title.length() > 20)
					{
						title = title.substring(0, title.length()-1);
						str = title.substring(0, title.indexOf(" "));
						if (!CommonFunction.isCharacter(str))
						{
							title = title.substring(title.indexOf(" "));
						}
						title = title.trim();
						break;
					}
				}
			}
			
			chunk = array[i].split(" ");
			if (chunk.length > 5)
			{
				for (int j=0; j<chunk.length; j++)
				{
					if (!CommonFunction.isCharacter(chunk[j]))
						break;
				}
				title = array[i];
				array[i] = CommonFunction.removePart(array[i], title, title);
				title = title.trim();
				break;
			}
		}
		return title;
	}
	
	/**
	 * getAuthors()
	 * - get the list of authors
	 * @return list of authors
	 */
	// check before, after words
	public String getAuthors()
	{
		String authors = "";
		String[] tmp = new String[100];
		Matcher leadingLetter;
		
		for (int i=0; i<array.length; i++)
		{
			leadingLetter = Pattern.compile("^([A-Z]{1})(\\.)( )*").matcher(array[i]);
			if (leadingLetter.find())
			{
				if (array[i].indexOf("and") >= 0) {
					authors = authors + (array[i].substring(0, array[i].indexOf("and"))).trim() + "& " + (array[i].substring(array[i].indexOf("and") + 3)).trim()  + "& ";
				} else {
					authors = authors + array[i].trim() + "& ";
				}
			
				array[i] = "";
			}
		}
		
		for (int i=0; i<array.length; i++)
		{
			if (array[i].indexOf(" and ") >= 0) {
				authors += (array[i].substring(0, array[i].indexOf(" and "))).trim() + "& " + (array[i].substring(array[i].indexOf(" and ") + 5)).trim()  + "& ";
			}
		}
		
		for (int i=0; i<array.length; i++)
		{
			if (array[i].trim().startsWith("and ")) {
				authors += (array[i].substring(4)).trim()  + "& ";
			}
		}
		
		if (authors == "") {
			authors = null;
		} else if ((authors.trim().endsWith(",")) || (authors.trim().endsWith("&"))) {
			authors = authors.trim().substring(0, authors.trim().length()-1);
		}
		
		return authors;
	}
	
	/**
	 * printArray()
	 * - print out the array for checking
	 * @param array
	 */
	public void printArray()
	{
		for (int i=0; i<array.length; i++) {
			System.out.println("array[" + i + "]: " + array[i]);			
		}
	}
}
