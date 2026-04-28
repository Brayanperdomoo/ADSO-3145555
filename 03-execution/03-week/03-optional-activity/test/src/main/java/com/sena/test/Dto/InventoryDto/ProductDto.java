package com.sena.test.Dto.InventoryDto;

public class ProductDto {

    public Integer id_product;
    public String name_product;
    public String description;
    public double price;
    
    public ProductDto(Integer id_product, String name_product, String description, double price) {
        this.id_product = id_product;
        this.name_product = name_product;
        this.description = description;
        this.price = price;
    }

    public Integer getId_product() {
        return id_product;
    }

    public void setId_product(Integer id_product) {
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

    
}