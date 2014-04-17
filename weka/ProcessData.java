package weka;

/*
 * update log
 * 
 * ===== 2014-02-24 =====
 * - created file
 * - processing data from 380 entries
 * ===== 2014-03-03 =====
 * - updated data processing (320 for training, 60 for testing)
 */

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessData {
	
	private int countWord(String str) {
		String[] tmp;
		tmp = str.split(" ");
		return tmp.length;
	}
	
	private int getKey(String str, String key) {
		Matcher matcher = Pattern.compile(key).matcher(str);
		if (matcher.find()) return 1;
		else return 0;
	}
	
	private int getInitials(String str) {
		String[] tmp;
		tmp = str.split(" ");
		int flag=0;
		for (int i=0; i<tmp.length; i++) {
			if (tmp[i].length()==1) flag = 1;
		}
		return flag;
	}
	
	private int getPrepo(String str) {
		String[] list = {"at", "in", "on", "of", "to", "with"};
		String[] tmp;
		tmp = str.split(" ");
		for (int i=0; i<tmp.length; i++) {
			for (int j=0; j<list.length; j++) {
				if (tmp[i].equals(list[j])) return 1;
			}
		}
		return 0;		
	}
	
	private int getLongest(String str) {
		String[] tmp;
		tmp = str.split(" ");
		int max=tmp[0].length();
		for (int i=1; i<tmp.length; i++) {
			if (tmp[i].length() > max) max = tmp[i].length();
		}
		return max;
	}
	
	public void makeDataset(int mod) {
		String filename = "webapps/FYP_UI/WEB-INF/classes/data/Publication.txt";
		String train = "webapps/FYP_UI/WEB-INF/classes/data/train-data.txt";
		String test = "webapps/FYP_UI/WEB-INF/classes/data/test-data.txt";
		//String filename = "src/data/Publication.txt";
		//String train = "src/data/train-data.txt";
		//String test = "src/data/test-data.txt";
		BufferedReader buf;
		BufferedWriter trainout, testout;
		String input;
		int cnt=0, flag=0, tr=0, te=0;
		
		try {
			buf = new BufferedReader(new FileReader(filename));
			trainout = new BufferedWriter(new FileWriter(train));
			testout = new BufferedWriter(new FileWriter(test));
			
			while ( (input = buf.readLine()) != null ) {
				if (input.matches("^[0-9]*\\.")) {
					if (cnt%mod!=0) {
						// training case
						flag = 0;
						tr++;
					} else {
						// testing set
						flag = 1;
						te++;
					}
					cnt++;
				}
				
				if (flag==0) trainout.write(input + "\n");
				else if (flag==1) testout.write(input + "\n");				
			}

			System.out.println(tr + " " + te);
			trainout.close();
			testout.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * makeArff()
	 * - use sample train and test data to generate arff file for later use
	 */
	public void makeArff() {
		String train = "webapps/FYP_UI/WEB-INF/classes/data/train-data.txt";
		String test = "webapps/FYP_UI/WEB-INF/classes/data/test-data.txt";
		//String train = "src/data/train-data.txt";
		//String test = "src/data/test-data.txt";
		BufferedReader buf = null;
		BufferedWriter trainout=null, testout=null, out=null;
		String traina = "webapps/FYP_UI/WEB-INF/classes/data/publication-train.arff";
		String testa = "webapps/FYP_UI/WEB-INF/classes/data/publication-test.arff";
		//String traina = "src/data/publication-train.arff";
		//String testa = "src/data/publication-test.arff";
		String input, output, str, type;
		int noOfWord, length, initials, prepo, longest, titleColon, ordinal;
		int[] keyword = new int[9];
		String[] name;
		HashMap<String, String> authors = new HashMap<String, String>();
		int acnt=0, tcnt=0, jcnt=0, pcnt=0, cnt=0;
		
		try {
			for (int round=0; round<2; round++) {
				// init
				acnt=0; tcnt=0; jcnt=0; pcnt=0; cnt=0;
				
				// change input files
				if (round==0) buf = new BufferedReader(new FileReader(train));
				else if (round==1) buf = new BufferedReader(new FileReader(test));
				
				// change output files
				if (round==0) {
					trainout = new BufferedWriter(new FileWriter(traina));
					out = trainout;
				}
				else if (round==1) {
					testout = new BufferedWriter(new FileWriter(testa));
					out = testout;
				}
				
				// add arff header
				out.write("@relation test\n\n");
				out.write("@attribute type {A,T,J,P}\n");
				out.write("@attribute no-of-word numeric\n");
				out.write("@attribute length numeric\n");
				out.write("@attribute initials {0,1}\n");
				out.write("@attribute key-journal {0,1}\n");
				out.write("@attribute key-proceeding {0,1}\n");
				out.write("@attribute key-ieee {0,1}\n");
				out.write("@attribute key-symposium {0,1}\n");
				out.write("@attribute key-workshop {0,1}\n");
				out.write("@attribute key-meeting {0,1}\n");
				out.write("@attribute key-conference {0,1}\n");
				out.write("@attribute key-transaction {0,1}\n");
				out.write("@attribute key-ordinal {0,1}\n");
				out.write("@attribute preposition {0,1}\n");
				out.write("@attribute longest-word numeric\n");
				out.write("@attribute colon-in-title {0,1}\n");
				out.write("@attribute ordinal {0,1}\n\n");
				out.write("@data\n");
				
				// add data
				while ( (input=buf.readLine()) != null) {
					str = "";
					output = "";
					for (int j=0; j<9; j++) keyword[j] = 0;
					
					if (input.indexOf("Authors=")>=0) {
						cnt++;
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
								ordinal = 0;
								
								output = type +","+ noOfWord +","+ length +","+ initials +","+ keyword[0]
										 +","+ keyword[1] +","+ keyword[2] +","+ keyword[3] +","+ keyword[4]
										 +","+ keyword[5] +","+ keyword[6] +","+ keyword[7] +","+ keyword[8]
										 +","+ prepo +","+ longest +","+ titleColon +","+ ordinal;
								out.write(output + "\n");
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
						ordinal = 0;
						
						output = type +","+ noOfWord +","+ length +","+ initials +","+ keyword[0]
								 +","+ keyword[1] +","+ keyword[2] +","+ keyword[3] +","+ keyword[4]
								 +","+ keyword[5] +","+ keyword[6] +","+ keyword[7] +","+ keyword[8]
								 +","+ prepo +","+ longest +","+ titleColon +","+ ordinal;
						out.write(output + "\n");
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
						ordinal = getKey(input, "[0-9]+(st|nd|rd|th)( )");
						
						output = type +","+ noOfWord +","+ length +","+ initials +","+ keyword[0]
								 +","+ keyword[1] +","+ keyword[2] +","+ keyword[3] +","+ keyword[4]
								 +","+ keyword[5] +","+ keyword[6] +","+ keyword[7] +","+ keyword[8]
								 +","+ prepo +","+ longest +","+ titleColon +","+ ordinal;
						out.write(output + "\n");
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
						else if (str.toLowerCase().indexOf("transaction")>=0) keyword[7] = 1;
						else {
							keyword[8] = getKey(str, ".*[0-9]*(st|nd|rd|th).*");
						}
						prepo = 0;
						longest = getLongest(str);
						titleColon = 0;
						ordinal = getKey(input, "[0-9]+(st|nd|rd|th)( )");
						
						output = type +","+ noOfWord +","+ length +","+ initials +","+ keyword[0]
								 +","+ keyword[1] +","+ keyword[2] +","+ keyword[3] +","+ keyword[4]
								 +","+ keyword[5] +","+ keyword[6] +","+ keyword[7] +","+ keyword[8]
								 +","+ prepo +","+ longest +","+ titleColon +","+ ordinal;
						out.write(output + "\n");
						pcnt++;
					}
				}
				System.out.println(acnt);
				System.out.println(tcnt);
				System.out.println(jcnt);
				System.out.println(pcnt);
				System.out.println(cnt + "\n");
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * makeArff2()
	 * @param data
	 * - use data from use to generate arff file
	 */
	public void makeArff2(String data) {
		try {
			String outfile = "webapps/FYP_UI/WEB-INF/classes/data/out-data.arff";
			//String outfile = "src/data/out-data.arff";
			BufferedWriter out = new BufferedWriter(new FileWriter(outfile));
			String[] chunk = data.split("%%");
			String input, output, str, type;
			int noOfWord, length, initials, prepo, longest, titleColon, ordinal;
			int[] keyword = new int[9];
			String[] name;
			HashMap<String, String> authors = new HashMap<String, String>();
			
			out.write("@relation test\n\n");
			out.write("@attribute type {A,T,J,P}\n");
			out.write("@attribute no-of-word numeric\n");
			out.write("@attribute length numeric\n");
			out.write("@attribute initials {0,1}\n");
			out.write("@attribute key-journal {0,1}\n");
			out.write("@attribute key-proceeding {0,1}\n");
			out.write("@attribute key-ieee {0,1}\n");
			out.write("@attribute key-symposium {0,1}\n");
			out.write("@attribute key-workshop {0,1}\n");
			out.write("@attribute key-meeting {0,1}\n");
			out.write("@attribute key-conference {0,1}\n");
			out.write("@attribute key-transaction {0,1}\n");
			out.write("@attribute key-ordinal {0,1}\n");
			out.write("@attribute preposition {0,1}\n");
			out.write("@attribute longest-word numeric\n");
			out.write("@attribute colon-in-title {0,1}\n");
			out.write("@attribute ordinal {0,1}\n\n");
			out.write("@data\n");
			
			for (int k=0; k<chunk.length; k++) {
				str = "";
				output = "";
				input = chunk[k];
				for (int j=0; j<9; j++) keyword[j] = 0;
				
				type = "A";
				noOfWord = countWord(input);
				length = input.length();
				initials = getKey(input, "[A-Z]{1}(\\.)");
				if (initials==0) initials = getInitials(input);							
				prepo = getPrepo(input);
				longest = getLongest(input);
				titleColon = 0;
				if (input.indexOf(":")>=0) titleColon = 1;
				ordinal = getKey(input, "[0-9]+(st|nd|rd|th)( )");
				
				output = type +","+ noOfWord +","+ length +","+ initials +","+ keyword[0]
						 +","+ keyword[1] +","+ keyword[2] +","+ keyword[3] +","+ keyword[4]
						 +","+ keyword[5] +","+ keyword[6] +","+ keyword[7] +","+ keyword[8]
						 +","+ prepo +","+ longest +","+ titleColon +","+ ordinal;
				out.write(output + "\n");
				
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
							ordinal = 0;
							
							output = type +","+ noOfWord +","+ length +","+ initials +","+ keyword[0]
									 +","+ keyword[1] +","+ keyword[2] +","+ keyword[3] +","+ keyword[4]
									 +","+ keyword[5] +","+ keyword[6] +","+ keyword[7] +","+ keyword[8]
									 +","+ prepo +","+ longest +","+ titleColon +","+ ordinal;
							out.write(output + "\n");
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
					ordinal = 0;
					
					output = type +","+ noOfWord +","+ length +","+ initials +","+ keyword[0]
							 +","+ keyword[1] +","+ keyword[2] +","+ keyword[3] +","+ keyword[4]
							 +","+ keyword[5] +","+ keyword[6] +","+ keyword[7] +","+ keyword[8]
							 +","+ prepo +","+ longest +","+ titleColon +","+ ordinal;
					out.write(output + "\n");
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
					ordinal = getKey(input, "[0-9]+(st|nd|rd|th)( )");
					
					output = type +","+ noOfWord +","+ length +","+ initials +","+ keyword[0]
							 +","+ keyword[1] +","+ keyword[2] +","+ keyword[3] +","+ keyword[4]
							 +","+ keyword[5] +","+ keyword[6] +","+ keyword[7] +","+ keyword[8]
							 +","+ prepo +","+ longest +","+ titleColon +","+ ordinal;
					out.write(output + "\n");
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
					else if (str.toLowerCase().indexOf("transaction")>=0) keyword[7] = 1;
					else {
						keyword[8] = getKey(str, ".*[0-9]*(st|nd|rd|th).*");
					}
					prepo = 0;
					longest = getLongest(str);
					titleColon = 0;
					ordinal = getKey(input, "[0-9]+(st|nd|rd|th)( )");
					
					output = type +","+ noOfWord +","+ length +","+ initials +","+ keyword[0]
							 +","+ keyword[1] +","+ keyword[2] +","+ keyword[3] +","+ keyword[4]
							 +","+ keyword[5] +","+ keyword[6] +","+ keyword[7] +","+ keyword[8]
							 +","+ prepo +","+ longest +","+ titleColon +","+ ordinal;
					out.write(output + "\n");
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	/*public static void main(String[] args) {
		// 791, 379, 262, 48
		
		// importance of each attribute & class*
		// strength of each approach
		// breakdown of fields (neighboring)
		
		// try:
		// - making format
		// - simple UI (easy for checking)
		
		makeDataset(15);
		makeArff();
	}*/

}
