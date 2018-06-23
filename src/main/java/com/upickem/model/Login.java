package com.upickem.model;

import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Entity
public class Login {

    @Id
    String username;
    String password;

    public Login() {

    }

    public Login(Long loginId, String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
