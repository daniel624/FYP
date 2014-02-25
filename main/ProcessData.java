package main;

/*
 * update log
 * 
 * ===== 2014-02-24 =====
 * - created file
 * - processing data from 380 entries
 */

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessData {
	
	public static int countWord(String str) {
		String[] tmp;
		tmp = str.split(" ");
		return tmp.length;
	}
	
	public static int getKey(String str, String key) {
		Matcher matcher = Pattern.compile(key).matcher(str);
		if (matcher.find()) return 1;
		else return 0;
	}
	
	public static int getInitials(String str) {
		String[] tmp;
		tmp = str.split(" ");
		int flag=0;
		for (int i=0; i<tmp.length; i++) {
			if (tmp[i].length()==1) flag = 1;
		}
		return flag;
	}
	
	public static int getPrepo(String str) {
		String[] list = {"at", "in", "on", "of", "to"};
		String[] tmp;
		tmp = str.split(" ");
		int flag=0;
		for (int i=0; i<tmp.length; i++) {
			for (int j=0; j<list.length; j++) {
				if (tmp[i].equals(list[j])) return 1;
			}
		}
		return 0;		
	}
	
	public static int getLongest(String str) {
		String[] tmp;
		tmp = str.split(" ");
		int max=tmp[0].length();
		for (int i=1; i<tmp.length; i++) {
			if (tmp[i].length() > max) max = tmp[i].length();
		}
		return max;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String filename = "src/data/Publication.txt";
		String outfile = "src/data/publication-train.arff";
		String outfile2 = "src/data/publication-test.arff";
		BufferedReader buf;
		BufferedWriter writer1, writer2;
		String input, output, str;
		String type;
		int noOfWord, length, initials, prepo, longest, titleColon;
		int[] keyword = new int[8];
		String[] name;
		HashMap<String, String> authors;
		int acnt=0, tcnt=0, jcnt=0, pcnt=0;
		// 791, 379, 262, 48
		
		try {
			authors = new HashMap<String, String>();
			buf = new BufferedReader(new FileReader(filename));
			writer1 = new BufferedWriter(new FileWriter(outfile));
			writer1.write("@relation test\n\n");
			writer1.write("@attribute type {A,T,J,P}\n");
			writer1.write("@attribute no-of-word numeric\n");
			writer1.write("@attribute length numeric\n");
			writer1.write("@attribute initials {0,1}\n");
			writer1.write("@attribute key-journal {0,1}\n");
			writer1.write("@attribute key-proceeding {0,1}\n");
			writer1.write("@attribute key-ieee {0,1}\n");
			writer1.write("@attribute key-symposium {0,1}\n");
			writer1.write("@attribute key-workshop {0,1}\n");
			writer1.write("@attribute key-meeting {0,1}\n");
			writer1.write("@attribute key-conference {0,1}\n");
			writer1.write("@attribute key-transaction {0,1}\n");
			writer1.write("@attribute preposition {0,1}\n");
			writer1.write("@attribute longest-word numeric\n");
			writer1.write("@attribute colon-in-title {0,1}\n\n");
			writer1.write("@data\n");
			writer2 = new BufferedWriter(new FileWriter(outfile2));
			writer2.write("@relation test\n\n");
			writer2.write("@attribute type {A,T,J,P}\n");
			writer2.write("@attribute no-of-word numeric\n");
			writer2.write("@attribute length numeric\n");
			writer2.write("@attribute initials {0,1}\n");
			writer2.write("@attribute key-journal {0,1}\n");
			writer2.write("@attribute key-proceeding {0,1}\n");
			writer2.write("@attribute key-ieee {0,1}\n");
			writer2.write("@attribute key-symposium {0,1}\n");
			writer2.write("@attribute key-workshop {0,1}\n");
			writer2.write("@attribute key-meeting {0,1}\n");
			writer2.write("@attribute key-conference {0,1}\n");
			writer2.write("@attribute key-transaction {0,1}\n");
			writer2.write("@attribute preposition {0,1}\n");
			writer2.write("@attribute longest-word numeric\n");
			writer2.write("@attribute colon-in-title {0,1}\n\n");
			writer2.write("@data\n");
			
			while ( (input=buf.readLine()) != null) {
				str = "";
				output = "";
				for (int j=0; j<8; j++) keyword[j] = 0;
				
				if (input.indexOf("Authors=")>=0) {					
					str = input.substring(8).trim();
					name = str.split("&");
					for (int i=0; i<name.length; i++) {
						name[i] = name[i].trim();
						if (!authors.containsKey(name[i])) {
							authors.put(name[i], name[i]);

							type = "A";
							noOfWord = countWord(name[i]);
							length = name[i].length();
							initials = getKey(name[i], "[A-Z]{1}(\\.)");
							if (initials==0) initials = getInitials(name[i]);							
							prepo = 0;
							longest = getLongest(name[i]);
							titleColon = 0;
							
							output = type +","+ noOfWord +","+ length +","+ initials +","+ keyword[0]
									 +","+ keyword[1] +","+ keyword[2] +","+ keyword[3] +","+ keyword[4]
									 +","+ keyword[5] +","+ keyword[6] +","+ keyword[7] +","+ prepo
									 +","+ longest +","+ titleColon;
							//System.out.println(output);
							if (acnt > 710) writer2.write(output + "\n");
							else writer1.write(output + "\n");
							acnt++;
						}
					}
				} else if (input.indexOf("Title=")>=0) {					
					str = input.substring(6).trim();
					
					type = "T";
					noOfWord = countWord(str);
					length = str.length();
					initials = 0;
					prepo = getPrepo(str);
					longest = getLongest(str);
					titleColon = 0;
					if (str.indexOf(":")>=0) titleColon = 1;
					
					output = type +","+ noOfWord +","+ length +","+ initials +","+ keyword[0]
							 +","+ keyword[1] +","+ keyword[2] +","+ keyword[3] +","+ keyword[4]
							 +","+ keyword[5] +","+ keyword[6] +","+ keyword[7] +","+ prepo
							 +","+ longest +","+ titleColon;
					//System.out.println(output);
					if (acnt > 341) writer2.write(output + "\n");
					else writer1.write(output + "\n");
					tcnt++;
				} else if (input.indexOf("Journal=")>=0) {					
					str = input.substring(8).trim();
					
					type = "J";
					noOfWord = countWord(str);
					length = str.length();
					initials = 0;
					if (str.toLowerCase().indexOf("journal")>=0) keyword[0] = 1;
					prepo = getPrepo(str);
					longest = getLongest(str);
					titleColon = 0;
					
					output = type +","+ noOfWord +","+ length +","+ initials +","+ keyword[0]
							 +","+ keyword[1] +","+ keyword[2] +","+ keyword[3] +","+ keyword[4]
							 +","+ keyword[5] +","+ keyword[6] +","+ keyword[7] +","+ prepo
							 +","+ longest +","+ titleColon;
					//System.out.println(output);
					if (acnt > 234) writer2.write(output + "\n");
					else writer1.write(output + "\n");
					jcnt++;
				} else if (input.indexOf("Proceeding=")>=0) {					
					str = input.substring(11).trim();
					
					type = "P";
					noOfWord = countWord(str);
					length = str.length();
					initials = 0;
					if (str.toLowerCase().indexOf("proceeding")>=0) keyword[1] = 1;
					else if (str.toLowerCase().indexOf("ieee")>=0) keyword[2] = 1;
					else if (str.toLowerCase().indexOf("symposium")>=0) keyword[3] = 1;
					else if (str.toLowerCase().indexOf("workshop")>=0) keyword[4] = 1;
					else if (str.toLowerCase().indexOf("meeting")>=0) keyword[5] = 1;
					else if (str.toLowerCase().indexOf("conference")>=0) keyword[6] = 1;
					prepo = 0;
					longest = getLongest(str);
					titleColon = 0;
					
					output = type +","+ noOfWord +","+ length +","+ initials +","+ keyword[0]
							 +","+ keyword[1] +","+ keyword[2] +","+ keyword[3] +","+ keyword[4]
							 +","+ keyword[5] +","+ keyword[6] +","+ keyword[7] +","+ prepo
							 +","+ longest +","+ titleColon;
					//System.out.println(output);
					if (acnt > 40) writer2.write(output + "\n");
					else writer1.write(output + "\n");
					pcnt++;
				}
				
				//if (output.length()>0) System.out.println(output);
			}
			System.out.println(acnt);
			System.out.println(tcnt);
			System.out.println(jcnt);
			System.out.println(pcnt);
			writer1.close();
			writer2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
