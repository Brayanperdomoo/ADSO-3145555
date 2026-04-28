package com.sena.test.Repository.InventoryRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sena.test.Entity.Inventory.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
    @Query(""
        + "SELECT "
        + "c "
        + "FROM "
        + "category c "
        +  "WHERE "
        + "c.name_category LIKE %?1% "
    )
    public List<Category> filterByFullName(String name_category);

}
