package com.Blogger.Blogger.service;

import com.example.blogger.entity.User;
import com.example.blogger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;


@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private String jwtSecret;

    public void sendMagicLink(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            user = new User();
            user.setEmail(email);
            userRepository.save(user);
        }

        String token = Jwts.builder()
                .setSubject(user.getId().toString())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        String magicLink = "http://localhost:8080/api/auth/magic-login/" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Magic Link");
        message.setText("Click here to login: " + magicLink);
        mailSender.send(message);
    }

    pulic String verifyMagicLink(String token){
        String userId = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow();
        return generateJwtToken(user);
    }

    private String generateJwtToken(User user){
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

}
