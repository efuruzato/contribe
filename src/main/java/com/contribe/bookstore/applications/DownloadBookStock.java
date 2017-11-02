package com.contribe.bookstore.applications;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;

import org.apache.log4j.Logger;

import com.contribe.bookstore.model.Book;
import com.contribe.bookstore.service.BookList;
import com.contribe.bookstore.service.BookListImpl;


/**
 * This class is used to initialize the book and stock tables. 
 * 
 * @author efuruzato
 *
 */
public class DownloadBookStock {
	
	static final String BOOK_STORE_DATA = "https://raw.githubusercontent.com/contribe/contribe/dev/bookstoredata/bookstoredata.txt";
	
	final static Logger logger = Logger.getLogger(DownloadBookStock.class);
/**
 * Download stock of books from the link BOOK_STORE_DATA
 * and adds the quantity of each book to the database.
 * 	
 * @param args
 * @throws Exception
 */
public static void main(String[] args) throws Exception {
	
	BookList bookList = new BookListImpl();

    URL bookStoreData = new URL(BOOK_STORE_DATA);
    BufferedReader in = new BufferedReader(
    new InputStreamReader(bookStoreData.openStream()));

    String inputLine;
    
    while ((inputLine = in.readLine()) != null) {
    	logger.info(inputLine);
    	String[] values = inputLine.split(";");
        if (values.length == 4) {
    		Book b = new Book();
    		b.setTitle(values[0]);
    		logger.info("title:"+values[0]);
    		b.setAuthor(values[1]);
    		logger.info("author:"+values[1]);
    		b.setPrice(new BigDecimal(values[2].replaceAll(",", "")));
    		logger.info("price:"+values[2]);
    		int quantity = Integer.valueOf(values[3]).intValue();
    		bookList.add(b,quantity);
       		logger.info("quantity:"+values[3]);
    		  		
    	}
    }
    in.close();
}
 }