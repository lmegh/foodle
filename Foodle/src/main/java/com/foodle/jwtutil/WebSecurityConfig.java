package com.foodle.jwtutil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig{

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

	  @Bean
	   public DaoAuthenticationProvider authenticationProvider() {
	       DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	        
	       authProvider.setUserDetailsService(jwtUserDetailsService);
	       authProvider.setPasswordEncoder(passwordEncoder());
	    
	       return authProvider;
	   }
	   
	   @Bean
	   public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
	     return authConfig.getAuthenticationManager();
	   }
	   
	   @Bean
	   public PasswordEncoder passwordEncoder() {
	     return new BCryptPasswordEncoder();
	   }
	
	   @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	      http.cors().and().csrf().disable()
	          .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
	          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
	          .authorizeHttpRequests()
             .requestMatchers("/login").permitAll();
	       
	      
	      http.authenticationProvider(authenticationProvider());
	
	      http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	      
	      return http.build();
	    }
}