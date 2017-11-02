package com.contribe.bookstore.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.contribe.bookstore.model.Basket;
import com.contribe.bookstore.model.Book;
import com.contribe.bookstore.service.BasketService;
import com.contribe.bookstore.service.BookList;
import com.contribe.bookstore.service.BookListImpl;

/**
 * Servlet implementation class ServletTest
 */
@WebServlet("/searchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	    BasketService service = new BasketService();
	    
	    BookList bookList = new BookListImpl();
	    
	    String searchString = request.getParameter("searchString");
	    
	    Book[] results = bookList.list(searchString); 
	    request.setAttribute("results", results);
	    
	    List<Basket> basket = service.listBasket();
	    request.setAttribute("basket", basket);
	    
	    BigDecimal total = service.addTotalBasketPrice();
	    request.setAttribute("total", total);
	    
		String nextJSP = "/bookstore.jsp";
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
		dispatcher.forward(request,response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
