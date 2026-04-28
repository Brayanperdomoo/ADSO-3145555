package com.sena.test.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.test.Dto.PersonDto;
import com.sena.test.Entity.Person;
import com.sena.test.IService.IPersonService;
import com.sena.test.Repository.PersonRepository;

@Service 
public class PersonService implements IPersonService{

    @Autowired
    private PersonRepository repo;

    @Override
    public List<Person> findAll() {
        return this.repo.findAll();
    }

    @Override
    public Person findById(int id){
        return repo.findById(id).orElse(null);
    }

    @Override
    public List<Person>filterByFullName(String name){
        return repo.filterByFullName(name);
    }

    @Override
    public String delete(int id){
        repo.deleteById(id);
        return null;
    }

    @Override
        public String save(PersonDto p) {
        Person person = new Person();
        person.setName(p.getName());
        person.setEdad(p.getEdad());
        repo.save(person);
        return "Persona guardado correctamente";
    }

}
