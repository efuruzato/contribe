package com.contribe.bookstore.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

/**
 * This class represents a Book in the bookstore.
 * @author efuruzato
 *
 */
@Indexed
@Entity(name = "Book")
@DiscriminatorValue("Book")
public class Book extends ShoppingItem {

	
	@Column(name="title")
	@Field
	private String title;
	
	@Column(name="author")
	@Field
	private String author;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	
}
