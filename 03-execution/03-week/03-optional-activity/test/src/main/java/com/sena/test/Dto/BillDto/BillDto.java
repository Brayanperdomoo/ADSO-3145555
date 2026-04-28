package com.sena.test.Dto.BillDto;

import java.time.LocalDate;

public class BillDto {

    private Integer id_bill;
    private LocalDate date;
    private double total;
    
    public BillDto(Integer id_bill, LocalDate date, double total) {
        this.id_bill = id_bill;
        this.date = date;
        this.total = total;
    }

    public Integer getId_bill() {
        return id_bill;
    }

    public void setId_bill(Integer id_bill) {
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