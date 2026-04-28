package com.sena.test.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity (name="users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column (name = "id_user")
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToOne 
    @JoinColumn (name = "idPerson")
    private Person Person;

    public User(){
        
    }

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    


}
