package com.agile.aggrement.invoice.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.agile.aggrement.invoice.model.User;
import com.agile.aggrement.invoice.repo.UserRepository;
import com.agile.aggrement.invoice.util.InvoiceUtility;



@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private InvoiceUtility invoiceUtility;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.findUserByUsername(email);

		if (user == null)
			throw new UsernameNotFoundException(String.format("No user exists with email : %s", email));
		else
			return JwtUserFactory.create(user);
	}

	public User getUserByEmail(String email) throws UsernameNotFoundException {
		User user = userRepo.findUserByUsername(email);

		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
		}else{
				return user;
		}
	}
	

	public User getUserByUserId(String id) throws UsernameNotFoundException {
		User user = userRepo.findOne(id);

		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with id '%s'.", id));
		} else {
			return user;
		}
	}

	
	
	

}
