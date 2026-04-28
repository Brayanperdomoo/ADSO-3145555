package com.sena.test.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.sena.test.Entity.Role;

@Repository
public interface RoleRepository extends JpaRepository <Role, Integer>{


    @Query(""
        + "SELECT  "
        + "r "
        + "FROM "
        + "role r "
        + "WHERE "
        + "r.role like %?1%  "
    )
    public List<Role>filterByFullName(String role);
}
