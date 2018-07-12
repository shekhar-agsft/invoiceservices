package com.agile.aggrement.invoice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.agile.aggrement.invoice.model.UserRole;
import com.agile.aggrement.invoice.model.UserRoleManagement;

/**
 * Query operations related to roles
 * 
 * @author shekhar
 *
 */
@Repository
public interface UserRoleRepository extends CrudRepository<UserRoleManagement, String>{

	@Query(value = "SELECT * FROM user_roles_manage t where t.username =:username", nativeQuery = true)
	List<UserRoleManagement> findRoleByUsername(@Param("username")String username);

	
	
}
