package com.sena.test.Repository.BillRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sena.test.Entity.Bill.BillEntity;

@Repository
public interface BillRespository extends JpaRepository <BillEntity,Integer> {
    
    //Fecha de la consulta 17/05/2025

    @Query(""
        + "SELECT "
        + "b "
        + "FROM "
        + "bill b "
        + "WHERE "
        + "b.date = ?1 "
    )
    public List<BillEntity>filterByDate(LocalDate date);
}
