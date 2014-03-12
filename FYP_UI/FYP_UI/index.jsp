<%@ page import="FYP_UI.*" %>

<html> 
<head> 
<Script> 
function clearFields(){ 
document.getElementById("intput_text_id").value=""; 
return false; 
}
</Script> 
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
<title>Automatic bibliography field recognition and format conversion</title> 
</head> 
<body style="font-family:verdana, sans-serif;">
	<font align="center" style="color: #5858FA" size="5"><b>Automatic bibliography field recognition and format conversion</b></font>
	<hr>
	
	<table>
		<tr>
			<td>
				<form action="Main" method="post"> 
					<font color="red" size="3">Type in the raw data in the below given text area and click on Execute button:</font>
					<br/><br/> 

					<textarea rows="15" cols="50" name="intput_text" id="intput_text_id"></textarea><br><br> 

					<input type="submit" value="Execute">    
					<input type="button" value="Reset" onClick="clearFields()"> 
				</form>
			</td>
		</tr>
		
		<tr>
			<td>
				<font color="red" size="3">Output result:</font>
					<br/><br/> 
				<textarea rows="15" cols="50" name="output_text"></textarea><br><br> 
			</td>
		</tr>
	</table>
	
	<hr>
	<div align="right">
		<font color="grey" size="1">
			<a href="http://www.cuhk.edu.hk">CUHK</a> |
			<a href="http://www.cse.cuhk.edu.hk">CSE Department</a> |
			<a href="http://www.erg.cuhk.edu.hk">Engineering Faculty</a> |
			<a href="/contact.html">Contact Us</a> |
			<br/>
			Copyright &copy; 2014 Department of Computer Science and Engineering, The Chinese University of Hong Kong. All rights reserved.
		</font>
	</div>
		
	</body>
</html>