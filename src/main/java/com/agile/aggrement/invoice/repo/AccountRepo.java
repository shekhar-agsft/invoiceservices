package com.agile.aggrement.invoice.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.agile.aggrement.invoice.model.AccountDetails;

@Repository
public interface AccountRepo extends CrudRepository<AccountDetails, Integer>{

}
