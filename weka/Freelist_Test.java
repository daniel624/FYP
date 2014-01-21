package weka;

import java.io.*;

public class Freelist_Test {
	public static void main(String[] args) {
		//String filename = "C:/Users/Lenovo/Documents/notes/FYP/freelist_20140118.txt";
		String filename = "C:/Users/Lenovo/Documents/workspace/FYP/src/data/freelist_new_part.txt";
		String input;
		String split1[] = new String[50];
		String split2[] = new String[50];
		String type;
		
		try {
			BufferedReader buf = new BufferedReader(new FileReader(filename));
			String outfilename = "C:/Users/Lenovo/Desktop/test.arff";
	    	BufferedWriter writer = new BufferedWriter(new FileWriter(outfilename));
	    	writer.write("@relation test\n\n");
	    	writer.write("@attribute no-of-word numeric\n");
	    	writer.write("@attribute length numeric\n");
	    	writer.write("@attribute type {A,T,JC}\n\n");
	    	writer.write("@data\n");
	    	
			input = buf.readLine();
			
			while (input!=null) {
				split1 = input.split(",");
				if (split1[1].equals("1")) type = "A";
				else if (split1[1].equals("2")) type = "T";
				else if (split1[1].equals("3")) type = "JC";
				else type = "?";
				
				split2 = split1[0].split(" ");
				writer.write(split2.length + "," + split1[0].length() + "," + type + "\n");
				
				/*System.out.println(input);
				System.out.println("No. of words: " + split.length);
				System.out.println("Input length: " + input.length());
				System.out.println("Type: " + type + "\n");*/
				
				input = buf.readLine();
			}

			writer.write("\n");
	    	writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
