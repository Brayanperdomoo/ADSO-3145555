package com.sena.test.IService.IBillService;

import java.time.LocalDate;
import java.util.List;

import com.sena.test.Dto.BillDto.BillDto;
import com.sena.test.Entity.Bill.BillEntity;

public interface IBillService {
    
    public List<BillEntity>findAll ();
    public BillEntity findById (int id);
    public List<BillEntity>filterByDate(LocalDate date);
    public String save (BillDto b);
    public String delete (int id);
}
