package com.sena.test.IService.IIventoryService;

import java.util.List;

import com.sena.test.Dto.InventoryDto.ProductDto;
import com.sena.test.Entity.Inventory.Product;

public interface IProductService {
    
    
    public Product findById(int id);
    public List<Product>findAll();
    public List<Product>filterByFullName(String name_product);
    public String save(ProductDto p);
    public String delete (int id);

}
