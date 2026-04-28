package com.sena.test.IService;

import java.util.List;

import com.sena.test.Dto.RoleDto;
import com.sena.test.Entity.Role;

public interface IRoleService {
    /*
	 * findAll: buscar todo
	 * findById: buscar por id
	 * filterByName: filtrar por nombre de categoria
	 * save: guardar
	 * delete: eliminar
	 */
    public List<Role>findAll();
    public Role findById (int id);
    public List<Role>filterByFullName(String role);
	public String save (RoleDto r);
    public String delete (int id);
    
}
