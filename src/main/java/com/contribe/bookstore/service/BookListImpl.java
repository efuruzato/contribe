package com.contribe.bookstore.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.contribe.bookstore.model.Book;
import com.contribe.bookstore.model.Stock;

/**
 * This class implements the operations on the book and stock tables.
 * 
 * @author efuruzato
 */
public class BookListImpl implements BookList {

    BookService service = new BookService();

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure()
                    .buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * If a search string is provided, search books with similar
     * title or author. If a search string is not provided,
     * return the list of all books.
     * 
     * There are two search implementations available: one with simple hql
     * and a more sophisticated full text search.
     */
    @Override
    public Book[] list(String searchString) {
        List<Book> books = service.fullTextSearch(searchString);
        if (books.isEmpty())
            return null;
        Book[] array = new Book[books.size()];
        for (int i=0; i<books.size(); i++){
        	array[i]=books.get(i);
        }
        return array;
    }

    /**
     * Adds book to the stock.
     * Special care was taken with transactions and atomicity of the
     * data operations for stock updates.
     */
    @Override
    public boolean add(Book book, int quantity) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx  = session.beginTransaction();
        
        //Find book in book table
        Book foundBook = service.searchBook(book, session);

        //If book found
        if (foundBook != null) {
        	//Find item in stock table
            Stock stock = service.getStockForItemId(foundBook.getId(), session);

            //If stock found
            if (stock != null) {
                Integer newQuantity = quantity + stock.getQuantity();
                stock.setQuantity(newQuantity);
                session.update(stock);
            } else {
                //If stock not found
                stock = new Stock();
                stock.setShoppingItem(foundBook);
                stock.setQuantity(quantity);
                session.save(stock);
            }
        } else {
            // book not found
            session.save(book);

            Stock stock = new Stock();
            stock.setShoppingItem(book);
            stock.setQuantity(quantity);
            session.save(stock);
        }
        tx.commit();

        return false;
    }

    /**
     * Buy books. Results may be:
     * OK(0)
     * NOT IN STOCK(1)
     * DOES NOT EXIST(2)
     * 
     * Special care was taken with transactions and atomicity of the
     * data operations for stock updates for each book.
     * 
     * @param books
     * @return
     */
    @Override
    public int[] buy(Book... books) {
        if (books == null) {
            return null;
        }

        int[] result = new int[books.length];

        for (int i = 0; i < books.length; i++) {
            result[i] = service.buy(books[i]);
        }

        return result;
    }

}
