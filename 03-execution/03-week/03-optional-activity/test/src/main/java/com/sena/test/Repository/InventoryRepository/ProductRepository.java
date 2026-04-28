package com.sena.test.Repository.InventoryRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sena.test.Entity.Inventory.Product;

@Repository
public interface ProductRepository extends JpaRepository <Product,Integer> {
    
    @Query (""
        + "SELECT "
        + "p "
        + "FROM "
        + "product p "
        + "WHERE "
        + "p.name_product LIKE%?1% "
    )
    public List<Product>filterByFullName(String name_product);
}