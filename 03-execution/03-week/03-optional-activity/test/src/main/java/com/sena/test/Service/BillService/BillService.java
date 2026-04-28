package com.sena.test.Service.BillService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.test.IService.IBillService.IBillService;
import com.sena.test.Repository.BillRepository.BillRespository;
import com.sena.test.Dto.BillDto.BillDto;
import com.sena.test.Entity.Bill.BillEntity;

@Service
public class BillService implements IBillService{
    
    @Autowired
    private BillRespository repo;

    @Override
    public List<BillEntity> findAll(){
        return this.repo.findAll();
    }

    @Override 
    public BillEntity findById(int id){
        return repo.findById(id).orElse(null);
    }

    @Override
    public List<BillEntity>filterByDate(LocalDate date){
        return repo.filterByDate(date);
    }

    @Override
    public String save(BillDto b){
        BillEntity bill = new BillEntity();
        bill.setDate(b.getDate());
        bill.setTotal(b.getTotal());
        repo.save(bill);
        return "La compra se guardo exitosamente";

    }
    
    @Override
    public String delete (int id){
        repo.deleteById(id);
        return null;
    }
}
