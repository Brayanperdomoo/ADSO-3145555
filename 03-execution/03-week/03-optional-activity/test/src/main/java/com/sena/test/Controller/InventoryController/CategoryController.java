package com.sena.test.Controller.InventoryController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.test.Dto.InventoryDto.CategoryDto;
import com.sena.test.Entity.Inventory.Category;
import com.sena.test.IService.IIventoryService.ICategoryService;


@RestController
@RequestMapping("/category")
public class CategoryController {
    
    @Autowired
    private ICategoryService service;

    @GetMapping("")
    public ResponseEntity<Object>findAll(){
    return new ResponseEntity<Object>(
    service.findAll(),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object>findById(
    @PathVariable int id){
        Category Category = service.findById(id);
        return new ResponseEntity<Object>
        (Category,HttpStatus.OK);

    }
    @GetMapping("filterByName/{name_category}")
    public ResponseEntity<Object>filterByName(
    @PathVariable String name_category){
        List<Category> Category=service.filterByFullName(name_category);
        return new ResponseEntity<Object>
        (Category,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Object>save(
    @RequestBody CategoryDto c){
        service.save(c);
        return new ResponseEntity<Object>
        ("Se guardo exitosamente",HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object>delete(
    @PathVariable int id){
        service.delete(id);
        return new ResponseEntity<Object>
        ("Se elimino correctamente",HttpStatus.OK);
    }
}
