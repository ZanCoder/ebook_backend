package com.ebook.controller;

import com.ebook.dto.OrderDTO;
import com.ebook.entity.Order;
import com.ebook.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@RequestBody OrderDTO orderRequestDTO) {
        int userId = orderRequestDTO.getUserId();
        int productId = orderRequestDTO.getProductId();

        ResponseEntity<?> response = orderService.addToCart(userId, productId);
        return response;
    }
}
