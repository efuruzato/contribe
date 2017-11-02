package com.contribe.bookstore.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.contribe.bookstore.model.Basket;
import com.contribe.bookstore.model.Book;
import com.contribe.bookstore.model.ShoppingItem;
import com.contribe.bookstore.service.BasketService;
import com.contribe.bookstore.service.BookList;
import com.contribe.bookstore.service.BookListImpl;

/**
 * Servlet implementation class ServletTest
 */
@WebServlet("/removeServlet")
public class RemoveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	    BasketService service = new BasketService();

	    String basketId = request.getParameter("basketId");
	    
        service.removeItemById(Integer.valueOf(basketId));	  	    
	    
		String next = "/searchServlet";
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(next);
		dispatcher.forward(request,response);


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
