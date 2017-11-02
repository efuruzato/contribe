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
<table class="table table-compact">

<tr>
<td><% String search = request.getParameter("searchString");
       if (search == null)
           search = ""; %>
        <form method="get" action="searchServlet">
            Search:<input type="text" name="searchString" value="<%=search %>"/><input type="submit" value="Search" />
            <br/>
        </form>

</td>
<td></td>
</tr>
<tr>
<td> <b>Catalog</b> 
<table class="table table-compact">


<% Book[] results = (Book[]) request.getAttribute("results"); 

   if (results != null && results.length > 0) { %>
<tr>
<th>Title</th>
<th>Author</th>
<th>Price</th>
<th></th>
</tr>

<%  for (Book result : results ) {%> 
<tr>
<td><%=result.getTitle()%></td>
<td><%=result.getAuthor()%></td>
<td><%=result.getPrice()%></td>
<td><a href="addServlet?bookId=<%=result.getId()%>">add to basket</a> </td>
<!-- <td><--%=result.getFailure_Data()% ></td> -->
</tr>
<% }
   } else {%>
<tr>No books found in the search.</tr>
<% }%>
</table>
</td>

<% List<Basket> basket = (List<Basket>) request.getAttribute("basket"); %>

<td> <b> Your Basket has <%=basket.size()%> items. </b> <br>
<%if (!basket.isEmpty()) { %>
Total Price: <%= request.getAttribute("total") %>
<br> <a href="buyServlet">Buy items in basket</a>

<table class="table table-compact">
<tr>
<th>Title</th>
<th>Author</th>
<th>Price</th>
<th></th>
<!-- <th>Failure Data</th> -->
</tr>

<% for (Basket item : basket ) {%> 
<tr>
<td><%=((Book)item.getShoppingItem()).getTitle()%></td>
<td><%=((Book)item.getShoppingItem()).getAuthor()%></td>
<td><%=((Book)item.getShoppingItem()).getPrice()%></td>
<td><a href="removeServlet?basketId=<%=item.getId()%>">remove</a></td>
</tr>
<% } %>

</table>
<% } %>

</td>
</tr>
</table>


</body>
</html>