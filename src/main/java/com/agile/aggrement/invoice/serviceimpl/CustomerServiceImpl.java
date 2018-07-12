package com.agile.aggrement.invoice.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.agile.aggrement.invoice.model.Customer;
import com.agile.aggrement.invoice.repo.CustomerRepository;
import com.agile.aggrement.invoice.services.CustomerService;

@Component
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository;

	@Override
	public void save(Customer requestDTO) {

		customerRepository.save(requestDTO);

	}

}
