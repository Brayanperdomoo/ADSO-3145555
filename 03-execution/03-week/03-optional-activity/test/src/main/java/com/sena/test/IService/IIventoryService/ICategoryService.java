package com.sena.test.IService.IIventoryService;

import java.util.List;

import com.sena.test.Dto.InventoryDto.CategoryDto;
import com.sena.test.Entity.Inventory.Category;

public interface ICategoryService {
    
    public List<Category>findAll();
    public Category findById (int id);
    public List<Category>filterByFullName(String name_category);
    public String save (CategoryDto c);
    public String delete(int id);

}
