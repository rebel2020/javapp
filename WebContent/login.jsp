<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login to thrillioWeb</title>
</head>
<body>
	<table>
		<tr>
		<td>
			<label>Login to thrillioWeb</label>
			</td>
		</tr>
		<form method=POST action="<%=request.getContextPath() %>/login">
		<tr>
			<td>
				<label>Email</label>
			</td>
			<td>
				<input name=email type=text>
			</td>
		</tr>
		<tr>
			<td>
				<label>Password</label>
			</td>
			<td>
				<input name=password type=text>
			</td>
		</tr>
		<tr>
		<td>
			<input type=submit value="Log in">
		</td>
		</tr>
		</form>
	</table>
</body>
</html>