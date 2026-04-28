package com.sena.test.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sena.test.Entity.User;

@Repository
public interface UserRepository extends JpaRepository <User, Integer> {

    @Query(""
        + "SELECT  "
        + "u "
        + "FROM "
        + "users u "
        + "WHERE "
        + "u.email like %?1%  "
    )
    public List<User>filterByFullName(String email);
}
