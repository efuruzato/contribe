package com.contribe.bookstore.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.contribe.bookstore.model.Basket;
import com.contribe.bookstore.model.Book;
import com.contribe.bookstore.model.ShoppingItem;
import com.contribe.bookstore.model.Stock;

/**
 * This class implements the interaction with the basket table in the database.
 * 
 * @author efuruzato
 */
public class BasketService {

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
     * Lists all books in basket
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Basket> listBasket() {
        String hql = "from Basket b";
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        TypedQuery<Basket> query = session.createQuery(hql);
        List<Basket> result = query.getResultList();
        tx.commit();
        return result;
    }
    
    /**
     * Adds total basket price
     * 
     * @return
     */
    public BigDecimal addTotalBasketPrice() {
        String hql = "from Basket b";
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        TypedQuery<Basket> query = session.createQuery(hql);
        List<Basket> result = query.getResultList();
        BigDecimal sum = BigDecimal.ZERO;
        for (Basket basket: result) {
        	ShoppingItem item = basket.getShoppingItem();
        	sum = sum.add(item.getPrice());
        }
        tx.commit();
        return sum;
    }


    /**
     * Adds item to basket.
     * 
     * @param shoppingItem
     * @return
     */
    public void addItem(ShoppingItem item) {
    	if (item == null)
    		return;
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        ShoppingItem s = (ShoppingItem) session.get(ShoppingItem.class, item.getId());
        if (s != null) {
        	Basket basket = new Basket();
        	basket.setShoppingItem(s);
        	session.save(basket);         	
        }
        tx.commit();
    }

    /**
     * Removes item from basket.
     * 
     * @param id
     */
    public void removeItemById(Integer basketId) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        Basket basket = (Basket) session.get(Basket.class, basketId);
        if (basket != null)
          session.delete(basket); 
        tx.commit();
    }
    
    /**
     * Removes item from basket.
     * 
     * @param id
     */
    public void removeItemByBookId(Integer bookId) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("From Basket as b where b.shoppingItem.id = ?0");
        query.setParameter(0, bookId);
        List<Basket> result = query.getResultList();
        if (result != null && !result.isEmpty()) {
        	Basket basket = result.get(0);
        	session.delete(basket);         	
        }
        tx.commit();
    }
    
    /**
     * Empty basket.
     * 
     */
    public void emptyBasket() {
        String hql = "from Basket b";
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        TypedQuery<Basket> query = session.createQuery(hql);
        List<Basket> result = query.getResultList();
        for(Basket basket: result) {
        	session.delete(basket);
        }
        tx.commit();
    }
    
    /**
     * Buys all books in basket.
     * 
     */
    public int[] buyAllBooksInBasket() {
    	List<Basket> result = listBasket();
        
        if (result.isEmpty())
        	return null;

        ArrayList<Book> books = new ArrayList<>();
        for(int i=0; i<result.size(); i++) {
        	ShoppingItem item = result.get(i).getShoppingItem();
        	if (item instanceof Book)
        	 books.add((Book) item);
        }
        
        Book[] bookArray = books.toArray(new Book[books.size()]);
        BookList bookList = new BookListImpl();
        int[] results = bookList.buy(bookArray);
        emptyBasket();
        return results;
    }

}
