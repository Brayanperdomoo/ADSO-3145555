package com.sena.test.Entity.Security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "User_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id_user_role")
    private int id_user_role;

    @ManyToOne
    @JoinColumn (name = "id_user")
    private User user;
    
    @ManyToOne
    @JoinColumn (name = "id_role")
    private Role role;

    public UserRole(){}

    public UserRole(int id_user_role, User user, Role role) {
        this.id_user_role = id_user_role;
        this.user = user;
        this.role = role;
    }

    public int getId_user_role() {
        return id_user_role;
    }

    public void setId_user_role(int id_user_role) {
        this.id_user_role = id_user_role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    


}
