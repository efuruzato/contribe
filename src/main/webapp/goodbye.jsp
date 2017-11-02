<%@page import="java.math.BigDecimal"%>
<%@page import="com.contribe.bookstore.service.BookList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.contribe.bookstore.model.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head><title>  </title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>


</head>

<body>

<!-- <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js" integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js" integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1" crossorigin="anonymous"></script>
-->


<% List<Basket> basket = (List<Basket>) request.getAttribute("oldBasket"); 
   int[] results = (int[]) request.getAttribute("results");%>

<%if (basket!= null && !basket.isEmpty()) { %>
Thank you for shopping with us!
<table class="table table-compact">
<tr>
<th>Title</th>
<th>Author</th>
<th>Price</th>
<th></th>
<!-- <th>Failure Data</th> -->
</tr>

<%	BigDecimal total = BigDecimal.ZERO;
    for (int i=0; i<basket.size(); i++ ) {

	String result = "";
	
    if (results[i] == BookList.OK) {
        result = "OK";  
        total = total.add(basket.get(i).getShoppingItem().getPrice());
    } else if (results[i] == BookList.NOT_IN_STOCK) {
        result = "NOT IN STOCK";
    } else if (results[i] == BookList.DOES_NOT_EXIST) {
        result = "DOES NOT EXIST";
    }%> 
<tr>
<td><%=((Book)basket.get(i).getShoppingItem()).getTitle()%></td>
<td><%=((Book)basket.get(i).getShoppingItem()).getAuthor()%></td>
<% if (results[i] == BookList.OK) { %>
<td><%=((Book)basket.get(i).getShoppingItem()).getPrice()%></td>
<% } else {%>
<td></td>
<% } %>
<td><%=result %></td>
</tr>
<% } %>

</table>
Total Price: <%= total %>
<% } %>
<br/><a href="searchServlet">Go back to bookstore</a>

</body>
</html>