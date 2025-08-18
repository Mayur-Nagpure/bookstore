package com.crni99.bookstore.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import com.crni99.bookstore.model.*;
import com.crni99.bookstore.repository.BookRepository;
import com.crni99.bookstore.repository.OrderBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crni99.bookstore.repository.BillingRepository;
import com.crni99.bookstore.repository.OrderRepository;

@Service
public class BillingService {

	private OrderRepository orderRepository;
	private BillingRepository billingRepository;
	@Autowired
	private OrderBillRepository orderBillRepository;
	@Autowired
	private BillPdfService billPdfService;
	@Autowired
	private BookRepository bookRepository;

	public BillingService(OrderRepository orderRepository, BillingRepository billingRepository) {
		this.orderRepository = orderRepository;
		this.billingRepository = billingRepository;
	}

	public Page<CustomerBooks> findPaginated(Pageable pageable, String term) {

		return page(pageable, term);
	}

	private Page<CustomerBooks> page(Pageable pageable, String term) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		List<Order> orders;
		List<CustomerBooks> list;

		if (term == null) {
			Iterable<Order> iterableOrders = orderRepository.findAll();
			 orders = new ArrayList<>();
			iterableOrders.forEach(orders::add);

		} else {
			LocalDate date = LocalDate.parse(term);
			orders = orderRepository.findByOrderDate(date);
		}

		// FIX: Use flatMapping to flatten book lists per customer
		Map<Customer, List<Book>> customerBooksMap = orders.stream()
				.collect(Collectors.groupingBy(
						Order::getCustomer,
						Collectors.flatMapping(order -> order.getBooks().stream(), Collectors.toList())
				));

		List<CustomerBooks> customerBooks = customerBooksMap.entrySet().stream()
				.map(entry -> new CustomerBooks(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());

		if (customerBooks.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, customerBooks.size());
			list = customerBooks.subList(startItem, toIndex);
		}

		return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), customerBooks.size());
	}


	@Transactional
	public Order createOrder(Customer customer, List<Book> books, String paymentMode) {
		billingRepository.save(customer);

		Order order = new Order();
		order.setCustomer(customer);
		order.setPaymentMode(paymentMode);
		order.setOrderDate(LocalDate.now());

		// Fetch managed book entities by IDs
		List<Book> managedBooks = books.stream()
				.map(book -> bookRepository.findById(book.getId())
						.orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + book.getId())))
				.collect(Collectors.toList());

		// Just set the books; no need for setOrder on Book entity
		order.setBooks(managedBooks);

		return orderRepository.save(order);
	}

	public List<CustomerBooks> findOrdersByCustomerId(Long id) {
		List<Order> orders = (List<Order>) orderRepository.findAll();

		// Group by customer and flatten all books from all orders for that customer
		Map<Customer, List<Book>> customerBooksMap = orders.stream()
				.collect(Collectors.groupingBy(
						Order::getCustomer,
						Collectors.flatMapping(
								order -> order.getBooks().stream(),
								Collectors.toList()
						)
				));

		// Only keep entry matching given customer id
		List<CustomerBooks> customerBooks = customerBooksMap.entrySet().stream()
				.filter(entry -> entry.getKey().getId().equals(id))   // filter by customer id
				.map(entry -> new CustomerBooks(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());

		return customerBooks;
	}

	public void deleteOrderById(Long id) {
		try {
			orderRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			// rethrow with a cleaner message or custom exception
			throw new RuntimeException("Order with ID " + id + " does not exist.", e);
		}
	}

	public void createOrderBill(Customer customer, Order order, List<Book> orderedBooks, double shippingCosts, double totalPrice) throws Exception {
		byte[] pdfBytes = billPdfService.generateBillPdf(customer, order, orderedBooks, shippingCosts, totalPrice);
		OrderBill bill = new OrderBill();
		bill.setOrderId(order.getId());
		bill.setCustomerId(customer.getId());
		bill.setCreatedDate(LocalDateTime.now());
		bill.setBillPdf(pdfBytes);
		orderBillRepository.save(bill);
	}

}