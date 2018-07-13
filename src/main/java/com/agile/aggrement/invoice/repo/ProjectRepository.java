package com.agile.aggrement.invoice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.agile.aggrement.invoice.model.Invoice;
import com.agile.aggrement.invoice.model.InvoiceProjectDetails;

@Repository
public interface ProjectRepository extends CrudRepository<InvoiceProjectDetails, Integer> {

	@Query(value = "SELECT * FROM invoice_project t where t.invoice_id = :invoiceId", nativeQuery = true)
	public List<InvoiceProjectDetails> findByInvoiceId(@Param("invoiceId") Invoice invoiceId);
}
