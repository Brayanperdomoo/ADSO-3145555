package com.sena.test.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity (name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id_role")
    private int id_role;

    @Column(name = "role")
    private String role;

    public Role(){
        
    }

    public Role(int id_role, String role) {
        this.id_role = id_role;
        this.role = role;
    }

    public int getId_role() {
        return id_role;
    }

    public void setId_role(int id_role) {
        this.id_role = id_role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}
