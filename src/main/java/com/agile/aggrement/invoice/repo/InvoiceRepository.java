package com.agile.aggrement.invoice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.agile.aggrement.invoice.model.Customer;
import com.agile.aggrement.invoice.model.Invoice;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Integer> {

	@Query(value = "SELECT * FROM invoice t where t.cust_id = :custId", nativeQuery = true)
	public List<Invoice> findByCustId(@Param("custId") Customer custId);

	/*@Query(value = "SELECT * FROM invoice t where t.invoice_number = :invoiceNumber", nativeQuery = true)
	public Invoice findByInvoiceNumber(@Param("invoiceNumber")int invoiceNumber);*/
}
