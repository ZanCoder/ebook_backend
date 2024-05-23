package com.ebook.service.impl;

import com.ebook.entity.Role;
import com.ebook.entity.User;
import com.ebook.handle.ErrorResponse;
import com.ebook.repository.RoleRepository;
import com.ebook.repository.UserRepository;
import com.ebook.service.EmailService;
import com.ebook.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(@Lazy BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public ResponseEntity<?> registerUser(User user) {
        // Kiểm tra username | email có tồn tại hay chưa?
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Tên đăng nhập đã tồn tại!"));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Địa chỉ email đã tồn tại!"));
        }

        // Mã hoá mật khẩu
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // Gán và gửi thông tin kích hoạt tài khoản
        user.setCodeActive(createCodeActive());
        user.setActive(false);

        // Nếu không bị 2 lỗi phía trên => Lưu user vào db
        User newUser = userRepository.save(user);

        // Sau khi save thành công thì gửi email cho người dùng để kích hoạt tài khoản
        sendEmailActiveUser(user.getEmail(), user.getCodeActive());

        return ResponseEntity.ok("Đăng ký thành công!");
    }

    public String createCodeActive() {
        // Tạo mã kích hoạt ngẫu nhiên
        return UUID.randomUUID().toString();
    }

    public void sendEmailActiveUser(String email, String codeActive) {
        String subject = "Kích Hoạt Tài Khoản - EBOOK";
        String text = "Vui lòng sử dụng mã sau để kích hoạt cho tài khoản <" + email + ">:<html><body><br/><b>" + codeActive + "</b></body></html>";
        text += "<br/> Hoặc Click vào đường link dưới đây để kích hoạt tài khoản: ";
        String url = "http://localhost:3000/active/"+email+"/"+codeActive;
        text += "<br/> <a href="+url+">" + url + "</a>";
        emailService.sendMessage("active@ebook.com", email, subject, text);
    }

    @Transactional
    public ResponseEntity<?> activeUser(String email, String codeActive) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Người dùng không tồn tại!"));
        }

        if (user.isActive()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Tài khoản đã được kích hoạt!"));
        }

        if (codeActive.equals(user.getCodeActive())) {
            user.setActive(true);
            userRepository.save(user);
            return ResponseEntity.ok("Kích hoạt tài khoản thành công!");
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("Mã kích hoạt không chính xác!"));
        }
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid Username or password");
        } else {
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), rolesToAuthorities(user.getRoleList()));
        }
    }

    private Collection<? extends GrantedAuthority> rolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getNameRole())).collect(Collectors.toList());
    }
}
