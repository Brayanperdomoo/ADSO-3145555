package com.sena.test.Dto.BillDto;

public class BillDetailDto {
    private Integer id_bill_detail;
    private double price;
    private int quantity;
    
    public BillDetailDto(Integer id_bill_detail, double price, int quantity) {
        this.id_bill_detail = id_bill_detail;
        this.price = price;
        this.quantity = quantity;
    }

    public Integer getId_bill_detail() {
        return id_bill_detail;
    }

    public void setId_bill_detail(Integer id_bill_detail) {
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
