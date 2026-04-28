package com.sena.test.Controller.InventoryController;

import org.springframework.web.bind.annotation.RestController;

import com.sena.test.Dto.InventoryDto.ProductDto;
import com.sena.test.Entity.Inventory.Product;

import com.sena.test.IService.IIventoryService.IProductService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService service;
    
    @GetMapping("")
     public ResponseEntity<Object>findAll(){
        return new ResponseEntity<Object>(
            service.findAll(),HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Object>save(
    @RequestBody ProductDto p){
        service.save(p);
        return new ResponseEntity<Object>
        ("Se guardo Exitosamente",HttpStatus.OK);
    }

    @GetMapping("{id}")        
    public ResponseEntity<Object>findById(
    @PathVariable int id){
        Product Product = service.findById(id);
        return new ResponseEntity<Object>
        (Product,HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object>delete(
    @PathVariable int id){
        service.delete(id);
        return new ResponseEntity<Object>
        ("Se elimno correctamente",HttpStatus.OK);

    }

    @GetMapping("/filterByName/{name_product}")
    public ResponseEntity<Object>filterByName(
    @PathVariable String name_product){
        List<Product>Product=service.filterByFullName(name_product);
        return new ResponseEntity<Object>
        (Product,HttpStatus.OK);
    }
}