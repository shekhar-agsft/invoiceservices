package com.agile.aggrement.invoice.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.agile.aggrement.invoice.model.InvoiceProjectDetails;

@Repository
public interface ProjectRepository extends CrudRepository<InvoiceProjectDetails, Integer> {

}
