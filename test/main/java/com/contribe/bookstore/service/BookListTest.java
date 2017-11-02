package com.contribe.bookstore.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import com.contribe.bookstore.model.Book;
import com.contribe.bookstore.model.Stock;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;


public class BookListTest{
 

    final static Logger logger = Logger.getLogger(BookListTest.class);
    
	BookList bookList = new BookListImpl();
	
    BookService service = new BookService();

	@Test
	public void testSearchBookTitle() {
		
	    logger.info("search for 'master'");
	    
		Book[] books = bookList.list("master");
		for(Book book: books) {
            logger.info("found book:"+book.getTitle()
            +","+ book.getAuthor() +","+ book.getPrice());
		}
		
		assertEquals(1, books.length);
		
	}
	
	   @Test
	    public void testSearchBookAuthor() {
	       
	       logger.info("search for 'bastard'");
	       
	        Book[] books = bookList.list("bastard");
	        for(Book book: books) {
	            logger.info("found book:"+book.getTitle()
	            +","+ book.getAuthor() +","+ book.getPrice());
	        }
	        
	     assertEquals(2, books.length);
	        
	    }
	
	    @Test
	    public void testSearchAllBooks() {
	        
	        logger.info("search for ''");
	        
	        Book[] books = bookList.list("");
	        for(Book book: books) {
	            logger.info("found book:"+book.getTitle()
	            +","+ book.getAuthor() +","+ book.getPrice());
	        }
	        
	        assertEquals(7, books.length);
	        
	    }
	    
	    @Test
	    public void testAddBook() {
	    	Book book = new Book();

	    	book.setTitle("Random Sales");
	    	book.setAuthor("Cunning Bastard");
	    	book.setPrice(new BigDecimal(499.50));
	    	
	    	Integer quantity = 3;
	    	Integer initialStock = service.getStockForBook(book);
	    	
	    	bookList.add(book, quantity);

	        Integer finalStock = service.getStockForBook(book);
	    	
	        assertTrue(finalStock == initialStock + quantity);
	    }
	    
	    @Test
	    public void testBuyBooks() {
	    	Book[] books = new Book[3];
	    	
	    	//Existing book
	    	Book book0 = new Book();
	    	book0.setTitle("Random Sales");
	    	book0.setAuthor("Cunning Bastard");
	    	book0.setPrice(new BigDecimal(499.50));
	    	books[0]=book0;
	    	Integer initialQuantity = service.getStockForBook(book0);
	    	assertTrue(initialQuantity > 0);

	    	
	    	//Book out of stock
	    	Book book1 = new Book();
	    	book1.setTitle("Desired");
	    	book1.setAuthor("Rich Bloke");
	    	book1.setPrice(new BigDecimal(564.50));
	    	books[1]=book1;
	    	Integer zeroQuantity = service.getStockForBook(book1);
	    	assertTrue(zeroQuantity == 0);
	    	
	    	//Book doesn't exist
	    	Book book2 = new Book();
	    	book2.setTitle("Doesn't Exist");
	    	book2.setAuthor("Bob Bloom");
	    	book2.setPrice(new BigDecimal(0.0));
	    	books[2]=book2;
	    	Integer negativeQuantity = service.getStockForBook(book2);
	    	assertTrue(negativeQuantity < 0);
	    	
	    	// Assert results
	    	int[] results = bookList.buy(books);
	    	assertTrue(results[0] == BookList.OK);
	    	assertTrue(results[1] == BookList.NOT_IN_STOCK);
	    	assertTrue(results[2] == BookList.DOES_NOT_EXIST);
	    	
	    	// Assert final quantities
	    	Integer finalQuantity = service.getStockForBook(book0);
	    	assertTrue(finalQuantity == initialQuantity - 1);
	    	
	    	zeroQuantity = service.getStockForBook(book1);
	    	assertTrue(zeroQuantity == 0);
	    	
	    	negativeQuantity = service.getStockForBook(book2);
	    	assertTrue(negativeQuantity < 0);
	    }
}