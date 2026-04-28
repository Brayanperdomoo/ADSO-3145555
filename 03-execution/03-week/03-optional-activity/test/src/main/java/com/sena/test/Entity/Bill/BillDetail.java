package com.sena.test.Entity.Bill;

import com.sena.test.Entity.Inventory.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "bill_detail")
public class BillDetail {
    
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column (name = "id_bill_detail")
    private int id_bill_detail;

    @Column (name = "price")
    private double price;

    @Column (name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "id_bill")
    private BillEntity bill;

    public BillDetail(){

    }

    public BillDetail(int id_bill_detail, double price, int quantity) {
        this.id_bill_detail = id_bill_detail;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId_bill_detail() {
        return id_bill_detail;
    }

    public void setId_bill_detail(int id_bill_detail) {
        this.id_bill_detail = id_bill_detail;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    
}

