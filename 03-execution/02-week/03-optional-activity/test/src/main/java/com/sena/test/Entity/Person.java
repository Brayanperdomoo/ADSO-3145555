package com.sena.test.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity (name = "person")

public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name ="idPerson")
    private int id;

    @Column(name = "name")
    private String name;

    @Column (name = "edad")
    private int edad;

    @OneToOne(mappedBy = "Person")
    private User User;

    public Person(){
        
    }

    public Person(int id, String name, int edad) {
        this.id = id;
        this.name = name;
        this.edad = edad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    
}
