package com.crni99.bookstore.repository;


import com.crni99.bookstore.model.OrderBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderBillRepository extends JpaRepository<OrderBill, Long> { }

