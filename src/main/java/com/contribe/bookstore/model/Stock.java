package com.contribe.bookstore.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * This class represents a stock item in the bookstore.
 * @author efuruzato
 *
 */
@Entity
@Table(name="stock")
public class Stock {


	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY )
	@Column(name="stock_id")
	private Integer id;
		

	@OneToOne(cascade=CascadeType.ALL)  
	@JoinColumn(name="shopping_item_id") 
	private ShoppingItem shoppingItem;
	
	@Column(name="quantity")
	private Integer quantity;

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	
    public ShoppingItem getShoppingItem() {
        return shoppingItem;
    }

    
    public void setShoppingItem(ShoppingItem shoppingItem) {
        this.shoppingItem = shoppingItem;
    }

    public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
