package FYP_UI;

import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;

public class HelloServlet extends HttpServlet {
  public void doPost (HttpServletRequest req,
                     HttpServletResponse res)
    throws ServletException, IOException
  {
    PrintWriter out = res.getWriter();
	String program = req.getParameter("program"); 
	
	
	out.println(program);
    out.println("Hello, world!");
    out.close();
  }
}
