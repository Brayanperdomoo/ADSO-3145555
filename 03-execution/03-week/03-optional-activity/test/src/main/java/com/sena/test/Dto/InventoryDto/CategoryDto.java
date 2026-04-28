package com.sena.test.Dto.InventoryDto;

public class CategoryDto {
    
    public Integer id_category;
    public String name_category;
    public String description;
    
    public CategoryDto(Integer id_category, String name_category, String description) {
        this.id_category = id_category;
        this.name_category = name_category;
        this.description = description;
    }

    public Integer getId_category() {
        return id_category;
    }

    public void setId_category(Integer id_category) {
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
