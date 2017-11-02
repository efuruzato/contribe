package com.contribe.bookstore.service;

import com.contribe.bookstore.model.Book;

/**
 * Interface of all methods interacting with the database.
 * 
 * @author efuruzato
 */
public interface BookList {


    public final Integer OK = 0;
    public final Integer NOT_IN_STOCK = 1;
    public final Integer DOES_NOT_EXIST = 2;
    /**
     * If a search string is provided, search books by
     * title or author. If a search string is not provided,
     * return the list of all books.
     * 
     * There are two implementations of the search available:
     * a full text search (used by default)
     * and a simple hql search.
     * 
     * @param searchString
     * @return
     */
    public Book[] list(String searchString);

    /**
     * Adds quantity of books to the stock.
     * 
     * @param book
     * @param quantity
     * @return
     */
    public boolean add(Book book, int quantity);

    /**
     * Buy books. Results may be:
     * OK(0)
     * NOT IN STOCK(1)
     * DOES NOT EXIST(2)
     * 
     * @param books
     * @return
     */
    public int[] buy(Book... books);

}
