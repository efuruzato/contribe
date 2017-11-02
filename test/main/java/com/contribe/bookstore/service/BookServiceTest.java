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


public class BookServiceTest{
    
    private static final SessionFactory sessionFactory;

    final static Logger logger = Logger.getLogger(BookServiceTest.class);
    
    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure()
                    .buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
    
	
	BookService service = new BookService();

	@Test
	public void testSearchBookTitle() {
		
		List<Book> books = service.searchBooks("master");
		for(Book book: books) {
			logger.info("found book:"+book.getTitle()
			+","+ book.getAuthor());
		}
		
		assertEquals(1, books.size());
		
	}
	
	   @Test
	    public void testSearchBookAuthor() {
	        
	        List<Book> books = service.searchBooks("bastard");
	        for(Book book: books) {
	            logger.info("found book:"+book.getTitle()
	            +","+ book.getAuthor());
	        }
	        
	     assertEquals(2, books.size());
	        
	    }
	
	@Test
	public void testGetStockForBook() {
		Book book = new Book();
		book.setTitle("Desired");
		book.setAuthor("Rich Bloke");
		book.setPrice( new BigDecimal("564.50"));
		
		Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
		
        Book foundBook = service.searchBook(book, session);
		Stock stock = service.getStockForItemId(foundBook.getId(), session);
		
		tx.commit();
		
		assertTrue(0 == stock.getQuantity());
	}
	
	@Test
	public void testBookExists() {
		Book book = new Book();
		book.setTitle("Generic Title");
		book.setAuthor("First Author");
		book.setPrice( new BigDecimal("185.50"));
	    
		Session session = sessionFactory.getCurrentSession();
	    Transaction tx = session.beginTransaction();
		
	    Book foundBook = service.searchBook(book, session);
		tx.commit();
		
		assertTrue(foundBook != null);

		logger.info("book id: "+foundBook.getId());
		
	}
 
}