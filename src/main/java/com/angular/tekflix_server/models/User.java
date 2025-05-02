package com.angular.tekflix_server.models;


import jakarta.persistence.*;
import lombok.Data;


@Data
@Table(name="users")
@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private ERole role;
}
