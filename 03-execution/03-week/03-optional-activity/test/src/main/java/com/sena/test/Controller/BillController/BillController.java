package com.sena.test.Controller.BillController;

import java.time.LocalDate;
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

import com.sena.test.Dto.BillDto.BillDto;
import com.sena.test.Entity.Bill.BillEntity;
import com.sena.test.IService.IBillService.IBillService;

@RestController
@RequestMapping("/bill")
public class BillController {
    
    @Autowired
    private IBillService service;

    @GetMapping("")
    public ResponseEntity<Object>findAll(){
    return new ResponseEntity<Object>(
    service.findAll(),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object>findById(
    @PathVariable int id){
        BillEntity BillEntity = service.findById(id);
        return new ResponseEntity<Object>
        (BillEntity,HttpStatus.OK);
    }

    @GetMapping("filterByName/{date}")
    public ResponseEntity<Object>filterByName(
    @PathVariable LocalDate date){
        List<BillEntity> Category=service.filterByDate(date);
        return new ResponseEntity<Object>
        (Category,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Object>save(
    @RequestBody BillDto b){
        service.save(b);
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
