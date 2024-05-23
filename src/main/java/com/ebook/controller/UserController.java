package com.ebook.controller;

import com.ebook.entity.User;
import com.ebook.security.JwtResponse;
import com.ebook.security.LoginRequest;
import com.ebook.service.JwtService;
import com.ebook.service.UserService;
import com.ebook.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
// @CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "*")
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody User user) {
        ResponseEntity<?> response = userServiceImpl.registerUser(user);
        return response;
    }

    @GetMapping("/active")
    public ResponseEntity<?> activeUser(@RequestParam String email, @RequestParam String codeActive) {
        ResponseEntity<?> response = userServiceImpl.activeUser(email, codeActive);
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Validated @RequestBody LoginRequest loginRequest) {
        // Xác thực username, password
        try {
            Authentication authentication = authenticationManager.authenticate(new
                    UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            // Nếu xác thực thành công => Tạo JWT
            if (authentication.isAuthenticated()) {
                final String jwt = jwtService.generateToken(loginRequest.getUsername());
                return ResponseEntity.ok(new JwtResponse(jwt));
            }
        } catch (AuthenticationException exception) {
            // Xác thực không thành công => return error and notification
            return ResponseEntity.badRequest().body("Invalid username or password");
        }

        return ResponseEntity.badRequest().body("Invalid username or password");
    }
}
