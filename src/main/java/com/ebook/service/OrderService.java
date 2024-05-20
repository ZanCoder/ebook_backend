package com.ebook.service;

import com.ebook.entity.Order;
import com.ebook.entity.OrderDetail;
import com.ebook.entity.Product;
import com.ebook.entity.User;
import com.ebook.repository.OrderDetailRepository;
import com.ebook.repository.OrderRepository;
import com.ebook.repository.ProductRepository;
import com.ebook.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public ResponseEntity<?> addToCart(int userId, int productId) {
        // Tạo đối tượng Order
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy product với id: " + productId));

        Order order = new Order();
        order.setUser(user);

        // Khởi tạo danh sách chi tiết đơn hàng nếu chưa tồn tại
        if (order.getOrderDetailList() == null) {
            order.setOrderDetailList(new ArrayList<>());
        }

        // Tạo đối tượng OrderDetail
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(product);

        // Thêm orderDetail vào danh sách chi tiết đơn hàng của order
        order.getOrderDetailList().add(orderDetail);

        // Lưu order vào cơ sở dữ liệu
        orderRepository.save(order);

        return ResponseEntity.ok("Thêm vào giỏ hàng thành công!");
    }
}
