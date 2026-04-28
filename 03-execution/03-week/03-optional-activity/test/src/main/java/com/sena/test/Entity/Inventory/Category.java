package com.sena.test.Entity.Inventory;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity(name = "category")

public class Category {

    @Id

    @GeneratedValue (strategy = GenerationType.IDENTITY)

    @Column(name = "id_category")
    private int id_category;

    @Column( name = "name_category")
    private String name_category;

    @Column (name = "description")
    private String description;

    @OneToMany(mappedBy = "category")
    private List<Product>products;

    public Category(){

    }

    public Category(int id_category, String name_category, String description) {
        this.id_category = id_category;
        this.name_category = name_category;
        this.description = description;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    public String getName_category() {
        return name_category;
    }

    public void setName_category(String name_category) {
        this.name_category = name_category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
}
