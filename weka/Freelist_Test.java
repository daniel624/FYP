package weka;

/*
*/

import java.io.*;
import data.*;

public class Freelist_Test {
	private static int getLongestWord(String field) {
		int length=0, max=0;
		
		for (int i=0; i<field.length(); i++) {
			if (field.charAt(i)==' ') {
				if (length > max) {
					max = length;
				}
				length = 0;
			} else {
				length++;
			}
		}
		
		if (length > max) max = length;
		
		return max;
	}
	
	public static void main(String[] args) {
		String filename, outfilename;
		BufferedReader buf;
		BufferedWriter writer;
		
		// training data
		//filename = "C:/Users/Lenovo/Documents/workspace/FYP/src/data/freelist_train_data.txt";
		//outfilename = "C:/Users/Lenovo/Documents/workspace/FYP/src/data/train.arff";
		
		// testing data
		filename = "C:/Users/Lenovo/Documents/workspace/FYP/src/data/freelist_test_data.txt";
		outfilename = "C:/Users/Lenovo/Documents/workspace/FYP/src/data/test.arff";
		
		String input;
		String split1[] = new String[50];
		String split2[] = new String[50];
		String type;
		int longest=0;
		
		try {
			buf = new BufferedReader(new FileReader(filename));
			
	    	writer = new BufferedWriter(new FileWriter(outfilename));
	    	writer.write("@relation test\n\n");
	    	writer.write("@attribute no-of-word numeric\n");
	    	writer.write("@attribute length numeric\n");
	    	writer.write("@attribute initials {0,1}\n");
	    	writer.write("@attribute type {A,T,JC}\n");
	    	writer.write("@attribute keyword {0,1}\n");
	    	writer.write("@attribute preposition {0,1}\n");
	    	writer.write("@attribute longest-word numeric\n");
	    	writer.write("@attribute colon-in-title {0,1}\n\n");
	    	writer.write("@data\n");
	    	
			input = buf.readLine();
			
			while (input!=null) {
				split1 = input.split(",");
				
				// check type
				if (split1[1].equals("1")) type = "A";
				else if (split1[1].equals("2")) type = "T";
				else if (split1[1].equals("3")) type = "JC";
				else type = "?";
				
				// get longest string within a field
				longest = getLongestWord(split1[0]);
				
				split2 = split1[0].split(" ");
				writer.write(split2.length + "," + split1[0].length() + "," + split1[2] + "," + 
							type + "," + split1[3] + "," + split1[4] + "," + longest + "," +
							split1[5] + "\n");
								
				input = buf.readLine();
			}

	    	writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
