package getdb_dblp;

import java.text.*;
import java.io.*;
import java.net.URL;

public class GetDBLPConference {
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
    			lv1 = new URL("http://www.informatik.uni-trier.de/~ley/db/conf/index-" + (char)k + ".html");
    			br1 = new BufferedReader(new InputStreamReader(lv1.openStream()));
    			filename = "conference-" + (char)k + ".txt";
    			//out = new PrintStream(new File(filename), "UTF-8");
    			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
    			
	    		while (null != (tmp1 = br1.readLine())){
	            	// each letter page
	            	if (tmp1.indexOf(key) > 0) {
	            		url = tmp1.substring(tmp1.indexOf(key)+12, tmp1.indexOf("</li>") + 5);

	            		if (url.indexOf("</a> -") >= 0) {
		            		url = url.replace("</a> -", "{");
		            		url = url.replace("</li>", "");
		            		url = url.replace("#38;", "");
		            		url = (url.substring(url.indexOf("{") + 1)).trim() + "{" + (url.substring(0, url.indexOf("{")).trim());
	            		} else {
	            			url = url.replace("</a>", "");
	            			url = url.replace("</li>", "{");
	            			url = url.replace("#38;", "");
	            		}
	            		
	            		// handling the link in after part
	            		if (url.indexOf("<a href=") >= 0) {
	            			url = url.replace("</a>", "");
	            			url = (url.substring(url.indexOf("\">") + 2)).trim();
	            		}
	            		
	            		System.out.println(url);
	            		
	            		out.append(url + "\r\n");
                		out.flush();
	            	}
	            }
    		}
			
    		// 3
    		lv1 = new URL("http://www.informatik.uni-trier.de/~ley/db/conf/index-" + "3" + ".html");
			br1 = new BufferedReader(new InputStreamReader(lv1.openStream()));
			filename = "conference-" + "3" + ".txt";
			//out = new PrintStream(new File(filename), "UTF-8");
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
			
    		while (null != (tmp1 = br1.readLine())){
            	// each letter page
            	if (tmp1.indexOf(key) > 0) {
            		url = tmp1.substring(tmp1.indexOf(key)+12, tmp1.indexOf("</li>") + 5);

            		if (url.indexOf("</a> -") >= 0) {
	            		url = url.replace("</a> -", "{");
	            		url = url.replace("</li>", "");
	            		url = (url.substring(url.indexOf("{") + 1)).trim() + "{" + (url.substring(0, url.indexOf("{")).trim());
            		} else {
            			url = url.replace("</a>", "");
            			url = url.replace("</li>", "{");
            		}
            		
            		System.out.println(url);
            		
            		out.append(url + "\r\n");
            		out.flush();
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
