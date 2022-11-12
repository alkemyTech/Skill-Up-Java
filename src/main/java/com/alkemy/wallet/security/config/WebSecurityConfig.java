package com.alkemy.wallet.security.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.alkemy.wallet.security.config.JwtTokenFilterConfigurer;
import com.alkemy.wallet.security.config.JwtTokenProvider;

@Configuration
public class WebSecurityConfig {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	//* aca se filtran los path de acuerdo a los accesos que tenga segun el rol asignado
	 @Bean
	 SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests()
		.antMatchers("/swagger-ui/**").permitAll()
		.antMatchers("/docs/**").permitAll()
		.antMatchers("/auth/**").permitAll()
				.antMatchers("/fixed-deposit/simulate").permitAll()
		.anyRequest().authenticated();
		
		http.exceptionHandling().accessDeniedPage("/login");
		http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
		
		return http.build();
	}
	
	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.components(new Components()
				.addSecuritySchemes("Bearer",
				new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT")));
	}
}
