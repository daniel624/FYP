<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="weka.TestWeka" %>
<%@ page import="ui.Test" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>

<%
	TestWeka sample = new TestWeka();
	sample.runResult();
	int test = sample.testMethod();
	int[] check = sample.checkResult();
	double[] label = sample.getClassLabel();
	double[][] distribution = sample.getClassDistribution();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Test Weka result page</title>
</head>
<body>

<p>Test: <%=test%></p>
<p>Test size: <%=sample.getTestSize()%></p>
<p>check[0]: <%=check[0]%></p>
<p>check[1]: <%=check[1]%></p>

<% if (label != null) { %>
<p>Weka result as follows:</p>
<table width=75%>
	<th>
		<td width=8%>Instance no.</td>
		<td width=18%>Author prob.</td>
		<td width=18%>Title prob.</td>
		<td width=18%>Journal prob.</td>
		<td width=18%>Proceeding prob.</td>
		<td>Predicted class</td>
	</th>
<% for (int i=0; i<label.length; i++) { %>
	<tr>
		<td><%=i%></td>
		<td><%=distribution[i][0]%></td>
		<td><%=distribution[i][1]%></td>
		<td><%=distribution[i][2]%></td>
		<td><%=distribution[i][3]%></td>
		<td>
			<%
			if (label[i]==0) out.println("Author");
			else if (label[i]==1) out.println("Title");
			else if (label[i]==2) out.println("Journal");
			else if (label[i]==3) out.println("Proceeding");
			%>
		</td>
	</tr>
<% } %>
</table>

<% } else {
	try {
		BufferedReader buf = new BufferedReader(new InputStreamReader(application.getResourceAsStream("/WEB-INF/classes/data/publication-test.arff")));
		String input;
		while ( (input = buf.readLine()) != null) {
			out.println(input + "<br>");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
} %>

</body>
</html>