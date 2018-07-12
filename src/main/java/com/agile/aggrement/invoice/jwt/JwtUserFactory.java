package com.agile.aggrement.invoice.jwt;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.agile.aggrement.invoice.model.User;
import com.agile.aggrement.invoice.model.UserRole;
import com.agile.aggrement.invoice.model.UserRoleManagement;
import com.agile.aggrement.invoice.repo.RoleRepository;
import com.agile.aggrement.invoice.repo.UserRoleRepository;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JwtUserFactory {

	@Autowired
	UserRoleRepository userRoleRepository;
	
	@Autowired
	RoleRepository roleRepository; 
	
	public JwtUser create(User user) {
		
		/*System.out.println(user);
		List<UserRoleManagement> userRoleManagement = userRoleRepository.findRoleByUsername(user.getUsername());
		List<Integer> intList = userRoleManagement.stream().map(i -> i.getUserRoleId()).collect(Collectors.toList());
		List<UserRole> userRoles = roleRepository.findByUserRoleId(intList);*/
		return new JwtUser(user.getUsername(), user.getPassword(),
				 mapToGrantedAuthorities(user.getUserRoles())); 
				
	}

	private static List<GrantedAuthority> mapToGrantedAuthorities(List<UserRole> authorities) {
		return authorities.stream().map(authority -> new SimpleGrantedAuthority(authority.getRoleName()))
				.collect(Collectors.toList());
	}
}
