package com.Blogger.Blogger.entity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;


@Entity
@Table(name="profiles")
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String name;
    private String bio;
    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name= "updated_at")
    private LocalDateTime updateAt = LocalDateTime.now();


}
