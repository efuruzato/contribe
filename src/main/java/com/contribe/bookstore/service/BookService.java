package com.contribe.bookstore.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;

import com.contribe.bookstore.model.Book;
import com.contribe.bookstore.model.Stock;

/**
 * This class implements the interaction with the book and stock tables in the database.
 * 
 * @author efuruzato
 */
public class BookService {

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure()
                    .buildSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            FullTextSession fullTextSession = Search.getFullTextSession(session);
            fullTextSession.createIndexer().startAndWait();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }


    /**
     * List all books
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Book> getAllBooks() {
        String hql = "from Book v";
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        TypedQuery<Book> query = session.createQuery(hql);
        List<Book> resultado = query.getResultList();
        tx.commit();
        return resultado;
    }

    /**
     * Return books with searchString like author or title.
     * If search string is empty, return all the books.
     * 
     * @param searchString
     * @return
     */
    public List<Book> searchBooks(String searchString) {

        if (searchString == null || searchString.isEmpty())
            return getAllBooks();

        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("From Book as b where b.title  like ?0 or b.author like ?1");
        query.setParameter(0, "%" + searchString + "%");
        query.setParameter(1, "%" + searchString + "%");
        List<Book> result = query.getResultList();
        tx.commit();
        return result;
    }
    
    
    /**
     * Return full text search on author or title.
     * If search string is empty, return all the books.
     * 
     * @param searchString
     * @return
     */
    public List<Book> fullTextSearch(String searchString) {
    	
    	if (searchString == null || searchString.isEmpty())
            return getAllBooks();

    	Session session = sessionFactory.getCurrentSession();
    	FullTextSession fullTextSession = Search.getFullTextSession(session);
    	Transaction tx = fullTextSession.beginTransaction();

    	// create native Lucene query unsing the query DSL
    	// alternatively you can write the Lucene query using the Lucene query parser
    	// or the Lucene programmatic API. The Hibernate Search DSL is recommended though
    	QueryBuilder qb = fullTextSession.getSearchFactory()
    	    .buildQueryBuilder().forEntity(Book.class).get();
    	
    	org.apache.lucene.search.Query query = qb
    	  .keyword()
    	  .onFields("title", "author")
    	  .matching(searchString)
    	  .createQuery();

    	// wrap Lucene query in a org.hibernate.Query
    	org.hibernate.query.Query hibQuery = 
    	    fullTextSession.createFullTextQuery(query, Book.class);

    	// execute search
    	List<Book> result = hibQuery.getResultList();
    	  
    	tx.commit();
    	session.close();
    	
    	return result;

    }

    /**
     * Search if a book with same title, author and price was
     * already created.
     * 
     * @param book
     * @return
     */
    public Book searchBook(Book book, Session session) {
        String hql = "From Book as b where b.title = ?0 and b.author = ?1 and b.price = ?2";
        Query query = session.createQuery(hql);
        query.setParameter(0, book.getTitle());
        query.setParameter(1, book.getAuthor());
        query.setParameter(2, book.getPrice());
        List<Book> resultado = query.getResultList();

        if (resultado.isEmpty()) {
            return null;
        } else {
            return resultado.get(0);
        }
    }

    /**
     * Search stock for the book id.
     * 
     * @param id
     * @param session
     * @return
     */
    public Stock getStockForItemId(Integer id, Session session) {
        String hql = "from Stock s where s.shoppingItem.id = " + id.toString();
        TypedQuery<Stock> query = session.createQuery(hql);
        List<Stock> resultado = query.getResultList();
        if (resultado.isEmpty()) {
            return null;
        } else {
            return resultado.get(0);
        }
    }
    
    /**
     * Search stock for the book.
     * 
     * @param book
     * @param session
     * @return
     */
    public Integer getStockForBook(Book book) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx  = session.beginTransaction();
        Book foundBook = searchBook(book, session);
        if (foundBook == null) {
        	tx.commit();
        	return -1;
        }
        Stock stock = getStockForItemId(foundBook.getId(), session);
        tx.commit();
        return stock.getQuantity();
    }
    
    /**
     * Buy a single book.
     * Special care was taken with transactions and atomicity of the
     * data operations for stock updates.
     * 
     * @param book
     * @return
     */
    public int buy(Book book) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        Integer bookId = book.getId();
        
        if (bookId == null || bookId == 0){
            Book foundBook = searchBook(book, session);
            if (foundBook != null)
              bookId = foundBook.getId();
            else
              bookId = -1;
        }
        
        Stock stock = getStockForItemId(bookId, session);
        
        if (stock != null) {
            int quantity = stock.getQuantity();
            if (quantity > 0) {
                stock.setQuantity(quantity - 1);
                session.update(stock);
                tx.commit();
                return BookList.OK;
            } else {
            	tx.commit();
                return BookList.NOT_IN_STOCK;
            }
        }
        tx.commit();
        return BookList.DOES_NOT_EXIST;
    }


}
