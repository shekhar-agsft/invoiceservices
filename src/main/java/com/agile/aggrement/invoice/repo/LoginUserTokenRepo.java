package com.agile.aggrement.invoice.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.agile.aggrement.invoice.model.UserLoginToken;

@Repository
public interface LoginUserTokenRepo extends CrudRepository<UserLoginToken, String> {

	@Query(value = "SELECT * FROM user_jwt_token t where t.user_email_id = :userEmailId AND t.token = :token", nativeQuery = true)
	public UserLoginToken findUserToken(@Param("userEmailId")String userEmailId,@Param("token") String token);
}
