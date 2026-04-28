package com.sena.test.Repository.BillRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import com.sena.test.Entity.Bill.BillDetail;
@Repository
public interface DetailBillRepository extends JpaRepository<BillDetail, Integer>{
    
}
