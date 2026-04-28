package com.sena.test.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sena.test.Entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query(""
        + "SELECT "
        + "p "
        + "FROM "
        + "person p "
        + "WHERE "
        + "p.name like %?1% "
    )
    public List<Person>filterByFullName(String name);

}
