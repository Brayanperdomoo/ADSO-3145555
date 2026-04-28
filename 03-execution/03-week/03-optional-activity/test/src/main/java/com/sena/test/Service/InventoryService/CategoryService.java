package com.sena.test.Service.InventoryService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.test.Dto.InventoryDto.CategoryDto;
import com.sena.test.Entity.Inventory.Category;
import com.sena.test.IService.IIventoryService.ICategoryService;
import com.sena.test.Repository.InventoryRepository.CategoryRepository;

@Service
public class CategoryService implements ICategoryService{
    
    @Autowired
    private CategoryRepository repo;

    @Override 
    public List<Category>findAll(){
        return this.repo.findAll();
    }
    
    @Override
    public Category findById(int id){
        return repo.findById(id).orElse(null);
    }

    @Override
    public List <Category>filterByFullName(String name_category){
        return repo.filterByFullName(name_category);
    }

    @Override
    public String save (CategoryDto c) {
        Category category = new Category();
        category.setName_category(c.getName_category());
        category.setDescription(c.getDescription());
        repo.save(category);
        return "La categoria se guardo exitosamente";
    }

    @Override
    public String delete (int id){
        repo.deleteById(id);
        return null;
    }
}
