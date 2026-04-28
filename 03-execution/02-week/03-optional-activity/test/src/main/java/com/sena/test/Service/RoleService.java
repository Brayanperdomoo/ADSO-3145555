package com.sena.test.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.test.Dto.RoleDto;
import com.sena.test.Entity.Role;
import com.sena.test.IService.IRoleService;
import com.sena.test.Repository.RoleRepository;



@Service
public class RoleService implements IRoleService{

    @Autowired
    private RoleRepository repo;

    @Override
    public List<Role>findAll(){
        return this.repo.findAll();
    }
    
    @Override
    public Role findById(int id){
        return repo.findById(id).orElse(null);
    }

    @Override
    public List<Role>filterByFullName(String role){
        return repo.filterByFullName(role);
    }

    @Override
    public String delete(int id){
        repo.deleteById(id);
        return null;
    }

    @Override
        public String save(RoleDto r) {
        Role role = new Role();
        role.setRole(r.getRole());
        repo.save(role);
        return "Role guardado correctamente";
    }


}
