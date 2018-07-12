package com.agile.aggrement.invoice.jwt;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author shekhar
 *
 */
@Configuration
@EnableJpaAuditing
public class InvoiceAppAuditor implements AuditorAware<String> {

	@Override
	public String getCurrentAuditor() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

		return ((UserDetails) authentication.getPrincipal()).getUsername();
	}

}
