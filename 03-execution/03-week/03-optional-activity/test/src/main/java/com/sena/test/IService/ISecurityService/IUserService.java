package com.sena.test.IService.ISecurityService;

import java.util.List;

import com.sena.test.Dto.SecurityDto.UserDto;
import com.sena.test.Entity.Security.User;

public interface IUserService {
    /*
	 * findAll: buscar todo
	 * findById: buscar por id
	 * filterByName: filtrar por nombre de categoria
	 * save: guardar
	 * delete: eliminar
	 */
    public List<User>findAll();
    public User findById (int id);
    public List<User>filterByFullName(String email);
	public String save (UserDto u);
    public String delete (int id);
}
