package com.crni99.bookstore.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity

public class OrderBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long customerId;
    private LocalDateTime createdDate;

    @Lob
    private byte[] billPdf; // PDF as byte array

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public byte[] getBillPdf() {
        return billPdf;
    }

    public void setBillPdf(byte[] billPdf) {
        this.billPdf = billPdf;
    }

}
