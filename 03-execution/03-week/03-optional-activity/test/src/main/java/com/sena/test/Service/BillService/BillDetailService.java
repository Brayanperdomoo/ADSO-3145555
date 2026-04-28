package com.sena.test.Service.BillService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.test.Dto.BillDto.BillDetailDto;
import com.sena.test.Entity.Bill.BillDetail;
import com.sena.test.IService.IBillService.IDetailBillService;
import com.sena.test.Repository.BillRepository.DetailBillRepository;

@Service
public class BillDetailService implements IDetailBillService {
    
    @Autowired
    private DetailBillRepository repo;

     @Override 
    public List<BillDetail>findAll(){
        return this.repo.findAll();
    }

    @Override
    public BillDetail findById(int id){
        return repo.findById(id).orElse(null);
    }

    @Override
    public String delete (int id){
        repo.deleteById(id);
        return null;
    }

    @Override 
    public String save(BillDetailDto b){
        BillDetail billDetail = new BillDetail();
        billDetail.setPrice(b.getPrice());
        billDetail.setQuantity(b.getQuantity());
        repo.save(billDetail);
        return "Se guardo Exitosamente";
            
    }
}
