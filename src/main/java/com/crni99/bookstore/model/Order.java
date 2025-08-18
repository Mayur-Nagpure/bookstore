package com.crni99.bookstore.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "order_date", nullable = false)
	private LocalDate orderDate;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	private String paymentMode;

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
			name = "order_books",
			joinColumns = @JoinColumn(name = "order_id"),
			inverseJoinColumns = @JoinColumn(name = "book_id")
	)
	private List<Book> books;

	public Order() {
	}

	public Order(Long id, LocalDate orderDate, Customer customer, List<Book> books, String paymentMode) {
		this.id = id;
		this.orderDate = orderDate;
		this.customer = customer;
		this.books = books;
		this.paymentMode = paymentMode;
	}


	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public void setPaymentMode(String paymentMode){
		this.paymentMode = paymentMode;
	}

	public String getPaymentMode(){
		return paymentMode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderDate=" + orderDate + ", customer=" + customer + ", book=" + books + " + paymentMode=" +paymentMode + "]";
	}

}
