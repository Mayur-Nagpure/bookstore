package com.crni99.bookstore.controller;

import java.util.List;

import javax.validation.Valid;

import com.crni99.bookstore.model.Order;
import com.crni99.bookstore.service.BillPdfService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.crni99.bookstore.model.Book;
import com.crni99.bookstore.model.Customer;
import com.crni99.bookstore.service.BillingService;
import com.crni99.bookstore.service.EmailService;
import com.crni99.bookstore.service.ShoppingCartService;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

	private final BillingService billingService;
	private final EmailService emailService;
	private final ShoppingCartService shoppingCartService;
	private final BillPdfService billPdfService;

	public CheckoutController(BillingService billingService, EmailService emailService,
			ShoppingCartService shoppingCartService, BillPdfService billPdfService) {
		this.billingService = billingService;
		this.emailService = emailService;
		this.shoppingCartService = shoppingCartService;
		this.billPdfService = billPdfService;
	}

	@GetMapping(value = { "", "/" })
	public String checkout(Model model) {
		List<Book> cart = shoppingCartService.getCart();
		if (cart.isEmpty()) {
			return "redirect:/cart";
		}
		model.addAttribute("customer", new Customer());
		model.addAttribute("productsInCart", cart);
		model.addAttribute("totalPrice", shoppingCartService.totalPrice().toString());
		model.addAttribute("shippingCosts", shoppingCartService.getshippingCosts());
		return "checkout";
	}

	@PostMapping("/placeOrder")
	public String placeOrder(@Valid Customer customer, BindingResult result, @RequestParam("payment") String paymentMode, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return "checkout";
		}

		// Create the order and get the Order object back (modify createOrder to return Order)
		Order order = billingService.createOrder(customer, shoppingCartService.getCart(), paymentMode);

		List<Book> cart = shoppingCartService.getCart();
		double shippingCosts = shoppingCartService.getshippingCosts();
		double totalPrice = shoppingCartService.totalPrice().doubleValue();

		try {
			billingService.createOrderBill(customer, order, cart, shippingCosts, totalPrice);
			byte[] billPdf = billPdfService.generateBillPdf(customer, order, cart, shippingCosts, totalPrice);


		String subject = "📚 Bookstore – Your Order is Confirmed! 🎉";

		String body = String.format("""
Dear %s,

🎉 Thank you so much for shopping with Bookstore!  
We’re absolutely thrilled to let you know that your order has been successfully **confirmed** and is now being prepared with great care.

Here’s what’s next for your order:
📦 We’ll carefully pack your books and get them on their way to your doorstep.  
📧 You’ll receive another email as soon as your order is shipped.

We truly appreciate your trust in us.  
Wishing you many exciting adventures between the pages of your new books! ❤️📖

Please find your order bill attached as a PDF.

Warm regards,  
The Bookstore Team
""", customer.getName());

			emailService.sendBillWithAttachment(customer.getEmail(), subject, body, billPdf);
		} catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute("warnMessage", "Order confirmed but bill could not be sent or saved.");
		}

		shoppingCartService.emptyCart();
		redirect.addFlashAttribute("successMessage", "The order is confirmed, kindly check your email.");
		return "redirect:/cart";
	}

}