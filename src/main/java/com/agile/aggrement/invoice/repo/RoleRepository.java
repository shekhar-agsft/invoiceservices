package com.agile.aggrement.invoice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.agile.aggrement.invoice.model.UserRole;

/**
 * Query operations related to roles
 * 
 * @author shekhar
 *
 */
@Repository
public interface RoleRepository extends CrudRepository<UserRole, String>{

	/*@Query(value = " SELECT * FROM user_roles WHERE role_id IN(:intList) ", nativeQuery = true)
	List<UserRole> findByUserRoleId(List<Integer> intList);*/

	
	
}
