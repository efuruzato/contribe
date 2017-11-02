package com.contribe.bookstore.applications;

import java.math.BigDecimal;
import java.util.*;

import com.contribe.bookstore.model.Basket;
import com.contribe.bookstore.model.Book;
import com.contribe.bookstore.model.ShoppingItem;
import com.contribe.bookstore.service.BasketService;
import com.contribe.bookstore.service.BookList;
import com.contribe.bookstore.service.BookListImpl;

class Main {

	BookList bookList = new BookListImpl();
	BasketService basketService = new BasketService();

	/**
	 * Display menu
	 */
	void displayMenu() {
		System.out.println("");
		System.out.println("Contribe Bookstore Menu:");
		System.out.println("1) Search books\n2) Add book to shopping cart\n"
				+ "3) Remove book from shopping cart\n4) List shopping cart\n"
				+ "5) Buy books from shooping cart\n"
				+ "6) Empty cart\n 7) Exit");
		System.out.print("Selection: ");
	}

	/**
	 * Search books
	 * @param in
	 */
	void searchBooks(Scanner in) {
		System.out.println("");
		System.out.println("1) Search books");
		System.out.println("Type searching word (Type ALL to list all books):");
		String searchString = in.next();
		Book[] books = null;
		if (searchString.equalsIgnoreCase("ALL"))
			books = bookList.list(null);
		else
			books = bookList.list(searchString);
		if (books != null) {
			System.out.println("Books found: " + books.length);
			for (Book book : books) {
				System.out
						.println(book.getId() + "," 
				        + book.getTitle() + "," 
						+ book.getAuthor() + "," 
				        + book.getPrice());
			}
		} else {
			System.out.println("Books found: 0");
		}
	}

	/**
	 * Add book to cart
	 * 
	 * @param in
	 */
	void addBookToCart(Scanner in) {
		System.out.println("");
		System.out.println("2) Add book to shopping cart");
		System.out.println("Type book id:");
		int bookId = readInt(in);
		Book book = new Book();
		book.setId(bookId);
		basketService.addItem(book);
		listCart();
	}
	
	/**
	 * Remove book from cart
	 * 
	 * @param in
	 */
	void removeBookFromCart(Scanner in) {
		System.out.println("");
		System.out.println("3) Remove book from shopping cart");
		System.out.println("Type book id:");
		int bookId = readInt(in);
		basketService.removeItemByBookId(bookId);
		listCart();
	}

	/**
	 * List cart
	 * 
	 */
	void listCart() {
		List<Basket> basket = basketService.listBasket();
		System.out.println("");
		System.out.println("Shopping cart");
		System.out.println("Items in the cart: " + basket.size());
		System.out.println("Cart Total Price: " + basketService.addTotalBasketPrice());
		for (Basket b : basket) {
			if (b.getShoppingItem() instanceof Book) {
				Book book = (Book) b.getShoppingItem();
				System.out
						.println(book.getId() + "," + book.getTitle() + "," + book.getAuthor() + "," + book.getPrice());
			} else {
            	ShoppingItem item = b.getShoppingItem();
            	System.out.println(item.getId() + ","
                        + item.getPrice());
            }
		}        
    }

	/**
	 * Buy items in cart
	 */
	void buyCart() {
        List<Basket> basket = basketService.listBasket();
        System.out.println("");
        System.out.println("5) Buy books from shooping cart");
        int[] results = basketService.buyAllBooksInBasket();
        BigDecimal total = BigDecimal.ZERO;
        int purchased = 0;
        for (int i=0; i < results.length; i++) {
        	Basket b = basket.get(i); 
            	String result = null;
            	if (results[i]==BookList.OK) {
            		result = "OK";
            		total = total.add(b.getShoppingItem().getPrice());
            		purchased = ++purchased;
            	} else if (results[i]==BookList.NOT_IN_STOCK)
            		result = "NOT IN STOCK";
            	else if (results[i]==BookList.DOES_NOT_EXIST)
            		result = "DOES NOT EXIST";
            	if (b.getShoppingItem() instanceof Book) {
                    Book book = (Book) b.getShoppingItem();
                    System.out.println(book.getId() + ","
                        + book.getTitle() + ","
                        + book.getAuthor()+ ","
                        + book.getPrice() + ","
                        + result);                
                } else {
                	ShoppingItem item = b.getShoppingItem();
                	System.out.println(item.getId() + ","
                            + item.getPrice() + ","
                            + result);
                }
        }
        System.out.println(purchased + " books purchased.");
        System.out.println("Total price:" + total);

	}
	
	/**
	 * Empty shopping cart
	 */
	void emptyCart() {
		basketService.emptyBasket();
		listCart();
	}
	
	/**
	 * Read integer only
	 */
	int readInt(Scanner in) {
		do{
	        if(in.hasNextInt()){
	            int result = in.nextInt();
	            return result;
	        }else{
	            in.nextLine();
	            System.err.println("Enter a valid Integer value");
	        }
	    }while(true);
	}

	public Main() {
		Scanner in = new Scanner(System.in);

		boolean loop = true;

		while (loop) {
			displayMenu();
			int option = readInt(in);
			switch (option) {
			case 1:
				searchBooks(in);
				break;
			case 2:
				addBookToCart(in);
				break;
			case 3:
				removeBookFromCart(in);
				break;
			case 4:
				listCart();
				break;
			case 5:
				buyCart();
				break;
			case 6:
				emptyCart();
				break;				
			case 7:
				System.out.println("Goodbye!");
				loop = false;
				break;
			default:
				System.err.println("Unrecognized option");
				break;
			}
		}
	}

	public static void main(String[] args) {
		new Main();
	}
}
