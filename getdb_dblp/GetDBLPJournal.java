package getdb_dblp;

import java.text.*;
import java.io.*;
import java.net.URL;

public class GetDBLPJournal {
	public static void main(String[] args)
	{
		PrintStream stdout = System.out;
		Writer out = null;
		try {
            URL lv1,lv2;
            BufferedReader br1,br2;
            String tmp1 = "",tmp2 = "";
            StringBuffer temp = new StringBuffer();
            String url = "";
            String extract = "";
            String fullname, shortname;
            int i=0;
    		String filename = "";
    		String key = "index.html";
    		
    		// a - z (97 -122)
    		for (int k=97; k<=122; k++) {
    			lv1 = new URL("http://www.informatik.uni-trier.de/~ley/db/journals/index-" + (char)k + ".html");
    			br1 = new BufferedReader(new InputStreamReader(lv1.openStream()));
    			filename = "journal-" + (char)k + ".txt";
    			//out = new PrintStream(new File(filename), "UTF-8");
    			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
    			
	    		while (null != (tmp1 = br1.readLine())){
	            	// each letter page
	            	if (tmp1.indexOf(key) > 0) {
	            		url = tmp1.substring(tmp1.indexOf("http://"), tmp1.indexOf(key)+10);
	            		System.out.println(url);
	            		
	            		lv2 = new URL(url); 	
	                    br2 = new BufferedReader(new InputStreamReader(lv2.openStream()));
	                    
	                    while (null != (tmp2 = br2.readLine())){
	                    	temp.append(tmp2);
	                    	//System.out.println(tmp2);
	                    }
	                    
	                    br2.close();
	                    
	                	if (temp.indexOf("id=\"headline\"") > 0) {
	                		// extract title
	                		extract = temp.substring(temp.indexOf("<div id=\"headline\">")+23, temp.indexOf("</h1></div>"));
	                		extract = extract.replace("#38;", "");
	                		
	                		// process title
	                		if (extract.indexOf("(") > 0) {
	                			fullname = extract.substring(0, extract.indexOf("(")).trim();
	                			shortname = extract.substring(extract.indexOf("(")+1, extract.indexOf(")")).trim();
	                		} else {
	                			fullname = extract.trim();
	                			shortname = "";
	                		}
	                		
	                		out.append(fullname + "," + shortname + "\r\n");
	                		out.flush();
	                		//System.out.println(fullname + "," + shortname);
	                		i++;
	                	}
	                	temp.setLength(0);
	            	}
	            }
    		}
    		//out of main page    
    		out.close();
            System.setOut(stdout);
        } catch (Exception ex) { 
            ex.printStackTrace();
        }
	}
}
