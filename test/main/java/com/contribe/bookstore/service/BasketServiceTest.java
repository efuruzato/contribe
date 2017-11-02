package com.contribe.bookstore.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.contribe.bookstore.model.Basket;
import com.contribe.bookstore.model.Book;
import com.contribe.bookstore.model.Stock;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

public class BasketServiceTest {

	final static Logger logger = Logger.getLogger(BasketServiceTest.class);

	BasketService service = new BasketService();

	BookService bookService = new BookService();

	BookList bookList = new BookListImpl();

	@Test
	public void testBasket() {

		// Empty basket
		service.emptyBasket();
		List<Basket> basketItems = service.listBasket();
		assertTrue(0 == basketItems.size());

		// Add one of each book to basket
		Book[] books = bookList.list("");
		logger.info(books.length + " books found.");
		logger.info("Adding books to basket:");
		for (Book book : books) {
			service.addItem(book);
			logger.info("adding book:" + book.getTitle());
		}

		// List basket
		basketItems = service.listBasket();
		assertTrue(books.length == basketItems.size());
		for (Basket basketItem : basketItems) {
			if (basketItem.getShoppingItem() instanceof Book) {
				Book book = (Book) basketItem.getShoppingItem();
				logger.info("found book in basket:" + book.getTitle() + "," + book.getAuthor() + "," + book.getPrice());
			}
		}
		
		// Check basket price
		BigDecimal basketPrice = service.addTotalBasketPrice();
		logger.info("basket price: "+basketPrice);
		
		BigDecimal sum = BigDecimal.ZERO;
		for (Book book : books) {
			sum = sum.add(book.getPrice());
		}
		assertTrue(sum.equals(basketPrice));

		// Store initial quantities
		ArrayList<Integer> initial = new ArrayList<>();
		for (Book book : books) {
			initial.add(bookService.getStockForBook(book));
		}
		
		// Buy all products in basket
		int[] results = service.buyAllBooksInBasket();
		
		// Store final quantities according to sale
		ArrayList<Integer> finalQuant = new ArrayList<>();
		for (Book book : books) {
			finalQuant.add(bookService.getStockForBook(book));
		}
		
		// Check each quantity
		for (int i=0; i<books.length; i++) {
			if (results[i] == BookList.OK) {
				assertTrue(finalQuant.get(i) == initial.get(i)-1);
			} else if (results[i] == BookList.NOT_IN_STOCK) {
				assertTrue(finalQuant.get(i) == initial.get(i));
			}
		}
		
		// Check that basket is empty
		basketItems = service.listBasket();
		assertTrue(0 == basketItems.size());

	}

}
