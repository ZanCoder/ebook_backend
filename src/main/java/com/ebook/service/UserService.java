package com.ebook.service;

import com.ebook.entity.User;
import com.ebook.handle.ErrorResponse;
import com.ebook.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> registerUser(User user) {
        // Kiểm tra username | email có tồn tại hay chưa?
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Tên đăng nhập đã tồn tại!"));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Địa chỉ email đã tồn tại!"));
        }

        // Nếu không bị 2 lỗi phía trên => Lưu user vào db
        User newUser = userRepository.save(user);

        return ResponseEntity.ok("Đăng ký thành công!");
    }
}
