package com.sena.test.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.test.Dto.UserDto;
import com.sena.test.Entity.User;
import com.sena.test.IService.IUserService;
import com.sena.test.Repository.UserRepository;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository repo;

    @Override
    public List<User> findAll() {
        return this.repo.findAll();
    }

    @Override
    public User findById(int id){
        return repo.findById(id).orElse(null);
    }

    @Override
    public List<User>filterByFullName(String name){
        return repo.filterByFullName(name);
    }

    @Override
    public String delete(int id){
        repo.deleteById(id);
        return null;
    }

    @Override
    public String save(UserDto u) {
        User user = new User();
        user.setEmail(u.getEmail());
        user.setPassword(u.getPassword());
        repo.save(user);
        return "User guardado correctamente";
    }

}

