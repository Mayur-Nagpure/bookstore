package com.crni99.bookstore.service;

import com.crni99.bookstore.model.Customer;
import com.crni99.bookstore.model.Order;
import com.crni99.bookstore.model.Book;
import org.springframework.stereotype.Service;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class BillPdfService {
    public byte[] generateBillPdf(Customer customer, Order order, List<Book> orderedBooks, double shippingCosts, double totalPrice) throws Exception {
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);
        PDPageContentStream content = new PDPageContentStream(doc, page);

        int pageStartX = 60;
        int y = 750;

        // Title and underline
        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD_OBLIQUE, 20);
        content.setNonStrokingColor(11, 96, 176); // Blue
        content.newLineAtOffset(70, y);
        content.showText("Bookstore - Order Bill");
        content.endText();
        content.setStrokingColor(11, 96, 176);
        content.moveTo(70, y - 5);
        content.lineTo(380, y - 5);
        content.stroke();

        // CUSTOMER DETAILS (no box)
        y -= 40;
        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 14);
        content.setNonStrokingColor(0, 0, 0);
        content.newLineAtOffset(pageStartX, y);
        content.showText("Customer Details:");
        content.setFont(PDType1Font.HELVETICA, 12);
        content.newLineAtOffset(0, -18);
        content.showText("Name: " + customer.getName() + " " + customer.getSurname());
        content.newLineAtOffset(0, -16);
        content.showText("Email: " + customer.getEmail());
        content.newLineAtOffset(0, -16);
        content.showText("Phone: " + customer.getPhoneNumber());
        content.endText();

        // ADDRESS (no box)
        y -= 75;
        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 14);
        content.setNonStrokingColor(0, 0, 0);
        content.newLineAtOffset(pageStartX, y);
        content.showText("Address:");
        content.setFont(PDType1Font.HELVETICA, 12);
        content.newLineAtOffset(0, -18);
        content.showText("Street & House: " + customer.getStreetAndHouseNumber());
        content.newLineAtOffset(0, -16);
        content.showText("City: " + customer.getCity());
        content.newLineAtOffset(0, -16);
        content.showText("Postal Code: " + customer.getPostalCode());
        content.newLineAtOffset(0, -16);
        content.showText("Country/Region: " + customer.getCountryRegion());
        content.endText();

        // ORDER INFORMATION
        y -= 90;
        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 14);
        content.setNonStrokingColor(0, 0, 0);
        content.newLineAtOffset(pageStartX, y);
        content.showText("Order Information:");
        content.setFont(PDType1Font.HELVETICA, 12);
        content.newLineAtOffset(0, -18);
        content.showText("Order ID: " + order.getId());
        content.newLineAtOffset(0, -16);
        content.showText("Order Date: " + order.getOrderDate().toString());
        content.newLineAtOffset(0, -16);
        content.endText();

        // PRODUCT TABLE
        y -= 55;
        int tableX = pageStartX;
        int nameColWidth = 390, priceColWidth = 80, tableWidth = nameColWidth + priceColWidth;
        int rowH = 20;
        int tableY = y;

        // Draw table header bg + border
        content.setNonStrokingColor(230, 230, 250); // Lavender
        content.addRect(tableX, tableY, tableWidth, rowH); content.fill();
        content.setStrokingColor(160, 160, 160); content.addRect(tableX, tableY, tableWidth, rowH); content.stroke();

        // Table header text
        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 12); content.setNonStrokingColor(11, 96, 176);
        content.newLineAtOffset(tableX + 10, tableY + 5);
        content.showText("Product");
        content.newLineAtOffset(nameColWidth, 0);
        content.showText("Price ($)");
        content.endText();

        // Product rows
        int currentRowY = tableY - rowH;
        content.setNonStrokingColor(0,0,0);
        for (Book book : orderedBooks) {
            content.setStrokingColor(210, 210, 210);
            content.addRect(tableX, currentRowY, tableWidth, rowH); content.stroke();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(tableX + 10, currentRowY + 5);
            content.showText(book.getName() + " by " + book.getAuthors());
            content.newLineAtOffset(nameColWidth, 0);
            content.showText(String.format("%.2f", book.getPrice()));
            content.endText();
            currentRowY -= rowH;   // move to next row
        }
        // Shipping Costs Row
        content.setStrokingColor(210, 210, 210);
        content.addRect(tableX, currentRowY, tableWidth, rowH); content.stroke();

        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 12);
        content.setNonStrokingColor(0, 0, 0);
        content.newLineAtOffset(tableX + 10, currentRowY + 5);
        content.showText("Shipping Costs");
        content.newLineAtOffset(nameColWidth, 0);
        content.showText("$" + String.format("%.2f", shippingCosts));
        content.endText();

        currentRowY -= rowH;
        // Total Price Row
        content.setStrokingColor(210, 210, 210);
        content.addRect(tableX, currentRowY, tableWidth, rowH); content.stroke();

        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 12);
        content.setNonStrokingColor(0, 0, 0);
        content.newLineAtOffset(tableX + 10, currentRowY + 5);
        content.showText("Total Price");
        content.newLineAtOffset(nameColWidth, 0);
        content.showText("$" + String.format("%.2f", totalPrice));
        content.endText();

        // PAYMENT MODE (outside/after all tables)
        int paymentY = currentRowY - 40;
        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 13);
        content.setNonStrokingColor(0,0,0);
        content.newLineAtOffset(tableX, paymentY);
        content.showText("Payment Mode: " + ("card".equals(order.getPaymentMode()) ? "Payment by card" : "Cash on delivery"));
        content.endText();

        content.close();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.save(out); doc.close();
        return out.toByteArray();
    }

}
