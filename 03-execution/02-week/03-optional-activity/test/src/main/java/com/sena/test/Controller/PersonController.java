package com.sena.test.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sena.test.Dto.PersonDto;
import com.sena.test.Entity.Person;
import com.sena.test.IService.IPersonService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/Person")
public class PersonController {

    @Autowired
    private IPersonService service;

    @GetMapping("")
    public ResponseEntity<Object>findAll(){
        return new ResponseEntity<Object>(
            service.findAll(),HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Object>save(
    @RequestBody PersonDto p){
        service.save(p);
        return new ResponseEntity<Object>("Se guardo Exitosamente",HttpStatus.OK);
    }


    @GetMapping("{id}")        
    public ResponseEntity<Object>findById(
    @PathVariable int id){
        Person Person = service.findById(id);
        return new ResponseEntity<Object>
        (Person,HttpStatus.OK);
    }

    @GetMapping("/filterByName/{full_name}")
    public ResponseEntity<Object>filterByName(
    @PathVariable String full_name){
        List<Person> person=service.filterByFullName(full_name);
        return new ResponseEntity<Object>
        (person,HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object>delete(
    @PathVariable int id){
        service.delete(id);
        return new ResponseEntity<Object>
        ("Se elimino correctamente",HttpStatus.OK);
    }
    
    
}
