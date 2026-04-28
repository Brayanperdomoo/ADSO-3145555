package com.sena.test.Entity.Bill;

import java.time.LocalDate;
import java.util.List;

import com.sena.test.Entity.Security.Person;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity (name = "bill")
public class BillEntity {
    
    @Id

    @GeneratedValue (strategy = GenerationType.IDENTITY)

    @Column (name = "id_bill")
    private int id_bill;
    
    @Column (name = "date")
    private LocalDate date;

    @Column (name = "total")
    private double total;
    
    @ManyToOne
    @JoinColumn(name = "id_person")
    private Person person;

    @OneToMany(mappedBy = "bill")
    private List<BillDetail> billDetails;

    public BillEntity(){}

    public BillEntity(int id_bill, LocalDate date, double total) {
        this.id_bill = id_bill;
        this.date = date;
        this.total = total;
    }

    public int getId_bill() {
        return id_bill;
    }

    public void setId_bill(int id_bill) {
        this.id_bill = id_bill;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    

}