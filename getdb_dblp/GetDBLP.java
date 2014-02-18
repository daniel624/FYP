package getdb_dblp;

/*
 *  GetDBLP
 *  This program is used to connect to DBLP and retrieve specific data from the site
 *  
 *  !! 2014-02-16 (Daniel)
 *  this file is temporary unused
 */

import java.io.*;
import java.net.URL;

public class GetDBLP {
	public static void main(String[] args)
	{
		PrintWriter out = null;
		try {
            URL lv1,lv2;
            BufferedReader br1,br2;
            StringBuffer temp = new StringBuffer();
            String tmp1 = "", tmp2 = "", url = "", extract = "";
            String fullname, shortname, filename;
    		//String use = "journal";
            //String use = "conf";
    		String key = "<a href=\"/Surname/";
    		String page = "";
    		//String path = "C:/Users/Lenovo/Documents/notes/FYP/journal/dblp/";
    		//String path = "C:/Users/Lenovo/Documents/notes/FYP/conf/dblp/";
    		String path = "C:/Users/Lenovo/Documents/notes/FYP/surname/";
    		
    		for (int k=0; k<1; k++) {
    			//page = "http://www.informatik.uni-trier.de/~ley/db/journals/index-" + (char) (97+k) + ".html";
    			//page = "http://www.informatik.uni-trier.de/~ley/db/conf/index-" + (char) (97+k) + ".html";
    			page = "http://www.surnamedb.com/Surname?alpha=" + (char) (97+k) + ".html";
    			lv1 = new URL(page);    			
    			br1 = new BufferedReader(new InputStreamReader(lv1.openStream()));
    			//filename = path + use + "-" + (char) (97+k) + ".txt";
    			filename = path + (char) (97+k) + ".txt";
    			//out = new PrintWriter(filename, "UTF-8");
    			
	    		while (null != (tmp1 = br1.readLine())){
	            	// each letter page
	    			System.out.println(tmp1);
	            	if (tmp1.indexOf(key) > 0) {
	            		url = tmp1.substring(tmp1.indexOf(key)+18, tmp1.indexOf("</a><br />"));
	            		System.out.println(url);
	            		
	            		/*lv2 = new URL(url); 	
	                    br2 = new BufferedReader(new InputStreamReader(lv2.openStream()));
	                    
	                    while (null != (tmp2 = br2.readLine())){
	                    	temp.append(tmp2);
	                    	//System.out.println(tmp2);
	                    }
	                    
	                    br2.close();
	                    
	                	if (temp.indexOf("id=\"headline\"") > 0) {
	                		// extract title
	                		extract = temp.substring(temp.indexOf("<div id=\"headline\">")+23, temp.indexOf("</h1></div>"));
	                		
	                		// process title
	                		if (extract.indexOf("(") > 0) {
	                			fullname = extract.substring(0, extract.indexOf("(")).trim();
	                			shortname = extract.substring(extract.indexOf("(")+1, extract.indexOf(")")).trim();
	                		} else {
	                			fullname = extract.trim();
	                			shortname = "";
	                		}
	                		
	                		out.println(fullname + "," + shortname);
	                		//System.out.println(fullname + "," + shortname);
	                	}
	                	temp.setLength(0);*/
	            	}
	            }
	    		System.out.println((char) (97+k) + " finished");
    		}
    		//out of main page    
    		//out.close();
        } catch (Exception ex) { 
            ex.printStackTrace();
        }
	}
}
