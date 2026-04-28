package com.sena.test.Service.InventoryService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.test.Dto.InventoryDto.ProductDto;
import com.sena.test.Entity.Inventory.Product;
import com.sena.test.IService.IIventoryService.IProductService;
import com.sena.test.Repository.InventoryRepository.ProductRepository;


@Service
public class ProductService implements IProductService{
    
    @Autowired
    private ProductRepository repo;

    @Override
    public List<Product>findAll(){
        return this.repo.findAll();
    }

    @Override
    public Product findById(int id){
        return repo.findById(id).orElse(null);
    }

    @Override
    public List <Product>filterByFullName(String name_product){
        return repo.filterByFullName(name_product);
    }


    @Override
    public String save(ProductDto p) {

        Product product = new Product();
        product.setName_product(p.getName_product());
        product.setDescription(p.getDescription());
        product.setPrice(p.getPrice());
        repo.save(product);
        return "El producto se guardó correctamente";

    }

    @Override
    public String delete (int id){
        repo.deleteById(id);
        return null;
    }

}
