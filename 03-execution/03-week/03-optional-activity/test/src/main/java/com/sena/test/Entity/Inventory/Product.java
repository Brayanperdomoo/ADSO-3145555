package com.sena.test.Entity.Inventory;

import java.util.List;

import com.sena.test.Entity.Bill.BillDetail;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity (name = "product")

public class Product {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column (name="id_product")
    private int id_product;

    @Column (name="name_product")
    private String name_product;

    @Column (name="description")
    private String description;

    @Column (name="Price")
    private double price;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List <BillDetail> billDetails;
    
    public Product(){

    }

    public Product(int id_product, String name_product, String description, double price) {
        this.id_product = id_product;
        this.name_product = name_product;
        this.description = description;
        this.price = price;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Product orElse(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElse'");
    }
    
    
}
