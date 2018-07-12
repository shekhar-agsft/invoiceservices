package com.agile.aggrement.invoice.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.agile.aggrement.invoice.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

	/*
	 * @Query(value =
	 * "SELECT * FROM user_jwt_token t where t.userEmailId = ?0 AND t.token = ?1"
	 * , nativeQuery = true) public Customer findUserToken(String userEmailId,
	 * String token);
	 */

}
