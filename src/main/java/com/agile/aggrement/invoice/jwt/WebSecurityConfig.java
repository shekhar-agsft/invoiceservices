package com.agile.aggrement.invoice.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// TODO: Auto-generated Javadoc
/**
 * The Class WebSecurityConfig used to add request filter,http request
 * authentication.
 */
@Configuration
@EnableWebSecurity
@EnableJpaAuditing
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthEntry entryPoint;

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(this.userDetailsService)
				.passwordEncoder(bCryptPasswordEncoder());
	}
	
	@Bean
	public SimpleCORSFilter simpleCORSFilterBean() throws Exception {
		return new SimpleCORSFilter();
	}

	@Bean
	public JwtAuthFilter authenticationTokenFilterBean() throws Exception {
		return new JwtAuthFilter();
	}

	/**
	 * Configure api call to ignore api request authorization
	 */
	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				// we don't need CSRF because our token is invulnerable
				.csrf().disable()

				.exceptionHandling().authenticationEntryPoint(entryPoint).and()

				// don't create session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

				.authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

				 // allow anonymous resource requests
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v2/api-docs/**",
                        "/configuration/**",
                        "/images/**",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/getCustomerDetails/**").permitAll()
                .antMatchers("/getAllCustomers/**").permitAll()
                .antMatchers("/savecustomer/**").permitAll()
                .antMatchers("/getSingleCustomer/**").permitAll()
                .antMatchers("/saveinvoice/**").permitAll()
                .antMatchers("/getInvoiceForCustomer/**").permitAll()
                .antMatchers("/saveprojects/**").permitAll()
                .antMatchers("/export/**").permitAll()
                .anyRequest().authenticated();
		// Custom JWT based security filter
		httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
		httpSecurity.addFilterBefore(simpleCORSFilterBean(), JwtAuthFilter.class);
		// disable page caching
		httpSecurity.headers().cacheControl();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/login/**", "/saveinvoice/**","/getAllCustomers/**","/getSingleCustomer/**","/getCustomerDetails/**","/export/**");
		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");	    
	}
	
	/**
	 * Configure global to encode userdetails servcie password.
	 *
	 * @param auth
	 *            the auth
	 * @throws Exception
	 *             the exception
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());

	}
}