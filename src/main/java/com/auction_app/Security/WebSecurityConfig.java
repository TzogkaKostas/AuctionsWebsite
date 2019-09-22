package com.auction_app.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/*
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}


	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// Setting Service to find User in the database.
		// And Setting PassswordEncoder
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
		 http.csrf().disable();
			//sessionManagement()
            //.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        //.and()
			//.authorizeRequests()
			//.antMatchers("/registration", "/home", "/get_users", "/get_appending_users",
			//		"/post_appending_user").permitAll();
			//.antMatchers( "/example", "/test_users").hasAnyRole("USER")
            //.anyRequest().authenticated();
		    //.and()
			//.formLogin()
            //.loginPage("/login")
            //.permitAll();
    }
}