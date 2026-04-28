package com.sena.test.Controller;

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

import com.sena.test.Dto.UserDto;
import com.sena.test.Entity.User;
import com.sena.test.IService.IUserService;


@RestController
@RequestMapping("/Users")
public class UserController {

    
    @Autowired
    private IUserService service;

    @GetMapping("")
    public ResponseEntity<Object>findAll(){
        return new ResponseEntity<Object>(
            service.findAll(),HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Object> save(
    @RequestBody UserDto u){
        service.save (u);
        return new ResponseEntity<Object>("Se guardo exitosamente",HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object>findById(
    @PathVariable int id){
        User User = service.findById(id);
        return new ResponseEntity<Object>
        (User,HttpStatus.OK);
    }

    @GetMapping("/filterByName/{full_name}")
    public ResponseEntity<Object>filterByName(
    @PathVariable String full_name){
        List<User> User = service.filterByFullName(full_name);
        return new ResponseEntity<Object>
        (User,HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object>delete(
    @PathVariable int id){
        service.delete(id);
        return new ResponseEntity<Object>
        ("Se elimino correctamente",HttpStatus.OK);
    }
}
