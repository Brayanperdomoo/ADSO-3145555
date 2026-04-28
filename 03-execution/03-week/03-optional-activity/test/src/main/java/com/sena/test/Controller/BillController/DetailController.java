package com.sena.test.Controller.BillController;

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

import com.sena.test.Dto.BillDto.BillDetailDto;

import com.sena.test.Entity.Bill.BillDetail;
import com.sena.test.IService.IBillService.IDetailBillService;

@RestController
@RequestMapping("/detailBill")
public class DetailController {

    @Autowired
    private IDetailBillService service;

    @GetMapping("")
    public ResponseEntity<Object>findAll(){
    return new ResponseEntity<Object>(
    service.findAll(),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object>findById(
    @PathVariable int id){
        BillDetail BillDetail = service.findById(id);
        return new ResponseEntity<Object>
        (BillDetail,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Object>save(
    @RequestBody BillDetailDto b){
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
