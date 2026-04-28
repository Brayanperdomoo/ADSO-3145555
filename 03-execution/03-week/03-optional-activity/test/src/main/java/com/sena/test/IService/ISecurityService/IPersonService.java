package com.sena.test.IService.ISecurityService;

import java.util.List;

import com.sena.test.Dto.SecurityDto.PersonDto;
import com.sena.test.Entity.Security.Person;

public interface IPersonService {
    /*
	 * findAll: buscar todo
	 * findById: buscar por id
	 * filterByName: filtrar por nombre de categoria
	 * save: guardar
	 * delete: eliminar
	 */
    public List<Person>findAll();
    public Person findById (int id);
    public List<Person>filterByFullName(String name);
	public String save (PersonDto p);
    public String delete (int id);
}
