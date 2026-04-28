package com.sena.test.IService.IBillService;

import java.util.List;

import com.sena.test.Dto.BillDto.BillDetailDto;
import com.sena.test.Entity.Bill.BillDetail;
public interface IDetailBillService {
    
    public List<BillDetail>findAll();
    public BillDetail findById (int id);
    public String save(BillDetailDto b);
    public String delete (int id);
}
