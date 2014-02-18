package isi;

/*
 * update log
 * 
 * ===== 2014-02-10 =====
 * - generate batch files to grab data from ISI
 * 
 * ===== 2014-02-11 =====
 * - extract wanted data from data grabbed from ISI
 * 
 * ===== 2014-02-13 =====
 * - successfully generate all required data from ISI
 * (full journal name, ISO abbreviated title, JCR abbreviated title, journal publisher)
 * 
 */

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;

public class GetISIList {
	
	/**
	 * getList()
	 * - get list of search result from ISI
	 */
	private static void getList() {
		try {
			//String tmp = java.io.File.
			String filename = "src/data/isi/bat/get_list_of_page.bat";
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			
			String url = "wget --cookies=on --keep-session-cookies --save-cookies=cookie.txt http://admin-router.webofknowledge.com/?DestApp=JCR --output-document=welcome.html";		
			writer.write(url + "\n");
			
			for (int i=1, j=1; i<8471; i+=20, j++) {
				url = "wget --cookies=on --load-cookies=cookie.txt --keep-session-cookies --save-cookies=cookie.txt --post-data \"edition=science&science_year=2012&social_year=2012&view=category&RQ=SELECT_ALL\" \"http://admin-apps.webofknowledge.com/JCR/JCR?RQ=SELECT_ALL&cursor="+i+"\" --output-document=../page/"+j+".html";
				writer.write(url + "\n");
			}
			
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * getURL()
	 * - get url for each journal from ISI
	 */
	private static void getURL() {
		try {
			String filename;
			BufferedReader buf;
			String input;
			String key = "http://admin-apps.webofknowledge.com/JCR/JCR?RQ=RECORD&rank=";
			String outfilename = "src/data/isi/full-url.txt";
		
			BufferedWriter writer = new BufferedWriter(new FileWriter(outfilename));
			for (int i=1, j=1; i<8471; i+=20, j++) {
				filename = "page/" + j + ".html";
				buf = new BufferedReader(new FileReader(filename));
				
				while ( (input = buf.readLine()) != null ) {
					if (input.indexOf(key) > 0) {
						writer.write(input.substring(input.indexOf(key), input.indexOf("\">")) + "\n");
					}
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * getPage()
	 * - get source code from each webpage
	 * - Full Journal Title, ISO Abbrev. Title, JCR Abbrev. Title, Publisher	
	 */
	private static void getPage() {
		try {
			String filename = "src/data/isi/full-url.txt";
			BufferedReader buf = new BufferedReader(new FileReader(filename));
			String input;
			String outfile;
			BufferedWriter writer = null;
			int cnt = 0;
			int current=0;
				
			while ( (input = buf.readLine()) != null ) {
				if (cnt%500==0) {
					// this if-block is to separate link-grabbing scripts into batch files of grabbing 500 pages each
					current = cnt;
					if (cnt>499) writer.close();
					outfile = "src/data/isi/bat/get_page"+cnt+".bat";
					writer = new BufferedWriter(new FileWriter(outfile));
					writer.write("wget --cookies=on --keep-session-cookies --load-cookies=../cookie.txt \"http://admin-router.webofknowledge.com/?DestApp=JCR\" --output-document=welcome.html\n");
					writer.write("timeout 2\n");
					writer.write("wget --cookies=on --keep-session-cookies --load-cookies=../cookie.txt \"http://admin-apps.webofknowledge.com/JCR/JCR?RQ=SELECT_ALL&cursor=1\" --output-document=list.html\n");
					writer.write("timeout 2\n");
				}
				
				writer.write("wget --cookies=on --keep-session-cookies --load-cookies=../cookie.txt " +
				//" --post-data \"edition=science&science_year=2012&social_year=2012&view=category&RQ=SELECT_ALL\"" +
				" \"" + input + "\" --output-document=../journal/"+current+"/page"+cnt+".html\n");
				writer.write("timeout 2\n");
				
				cnt++;
			}
			writer.close();
			buf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * getData()
	 * - get wanted info from each source page
	 */
	private static void getData() {
		String filename, path="", tmp;
		BufferedReader buf;
		String key1, key2, key3, key4;
		int mark=0;
		String str,data;
				
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/isi/ISI-data.txt"));
			for (int i=0; i<8471; i++) {
			//int i=0;
				data = "";
				if (i%500==0) {
					path = "journal/" + i;
					System.out.println(i);
				}
				filename = path + "/page"+i+".html";
				buf = new BufferedReader(new FileReader(filename));
				while (null != (tmp = buf.readLine())){
	            	key1 = "Full Journal Title:";
					key2 = "ISO Abbrev. Title:";
					key3 = "JCR Abbrev. Title:";
					key4 = "Publisher:";
					if (tmp.indexOf(key1) > 0) {
						tmp = buf.readLine();
						str = tmp.substring(tmp.indexOf("\">")+2, tmp.indexOf("&nbsp;")).trim();
						data += str + "%%";
					}
					if (tmp.indexOf(key2) > 0) {
						tmp = buf.readLine();
						str = tmp.substring(tmp.indexOf("\">")+2, tmp.indexOf("</td>")).trim();
						data += str + "%%";
					}
					if (tmp.indexOf(key3) > 0) {
						tmp = buf.readLine();
						str = tmp.substring(tmp.indexOf("\">")+2, tmp.indexOf("</td>")).trim();
						data += str + "%%";
					}
					if (tmp.indexOf(key4) > 0) {
						tmp = buf.readLine();
						str = tmp.substring(tmp.indexOf("\">")+2, tmp.indexOf("</td>")).trim();
						data += str;
					}
	            }
				//System.out.println(data);
				writer.write(data + "\n");
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		getList();
		//getURL();
		//getPage();
		//getData();
	}

}
