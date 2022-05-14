package com.example.talkline.entities;

import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer userId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "subscribe_date")
    private String subscribeDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    public String getFullName(){
        return firstName + " " + lastName;
    }

    public String getCustomLocation(){
        return location.getCity() + ", " + location.getDistrict();
    }

    public void prepareInsert(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        password = encoder.encode(password);
        profilePicture = "/img/blank_profile.png";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        subscribeDate = dtf.format(now);
    }
}
