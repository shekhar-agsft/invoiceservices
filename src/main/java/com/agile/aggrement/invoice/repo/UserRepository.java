/**
 * 
 */
package com.agile.aggrement.invoice.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.agile.aggrement.invoice.model.User;

/**
 * @author shekhar
 *
 */
@Repository
public interface UserRepository extends CrudRepository<User, String> {

	User findUserByUsername(String email);

	
}
