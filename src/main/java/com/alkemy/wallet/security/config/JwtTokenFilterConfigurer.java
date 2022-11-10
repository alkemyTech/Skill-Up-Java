package com.alkemy.wallet.security.config;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>{

	private JwtTokenProvider jwtTokenProvider;
	
	public JwtTokenFilterConfigurer(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public void configure(HttpSecurity httpSecurity) throws Exception {
		JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenProvider);
		httpSecurity.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
