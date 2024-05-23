package com.ebook.service;

import com.ebook.entity.Role;
import com.ebook.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtService {
    private static final String SECRET_KEY = "t3VZkJF2NVk4eW9OS2p6QU5tYnpJYVp5alZ0b0o0TzF3U05WTlRtV3lrcz0=";

    @Autowired
    private UserService userService;

    // Tạo JWT dựa trên username
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        User user = userService.findByUsername(username);

        boolean isAdmin = false;
        boolean isStaff = false;
        boolean isUser = false;
        if (user != null && user.getRoleList().size() > 0) {
            List<Role> roles = user.getRoleList();
            for (Role role : roles) {
                if (role.getNameRole().equals("ADMIN")) {
                    isAdmin = true;
                }
                if (role.getNameRole().equals("STAFF")) {
                    isStaff = true;
                }
                if (role.getNameRole().equals("USER")) {
                    isUser = true;
                }
            }
        }

        claims.put("isAdmin", isAdmin);
        claims.put("isStaff", isStaff);
        claims.put("isUser", isUser);

        return createToken(claims, username);
    }

    // Tạo JWT với các claim đã chọn
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // Hết hạn sau 30p
                .signWith(SignatureAlgorithm.HS256, getSignedKey())
                .compact();
    }

    // Lấy SECRET_KEY
    private Key getSignedKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Kiểm tra toàn bộ thông tin
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSignedKey()).parseClaimsJws(token).getBody();
    }

    // Kiểm tra thông tin cho 1 claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Kiểm tra thời gian hết hạn từ JWT
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Kiểm tra username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Kiểm tra JWT còn hạn sử dụng không?
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Kiểm tra token có hợp lệ hay không?
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}