package com.crni99.bookstore.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService;
	private DataSource securityDataSource;
	
	public SecurityConfig(DataSource securityDataSource) {
		this.securityDataSource = securityDataSource;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.jdbcAuthentication()
				.dataSource(securityDataSource)
				.usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?")
				.authoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username = ?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/user-login", "/css/**", "/js/**", "/images/**", "/h2-console/**", "/admin-login").permitAll()
				.antMatchers( "/book/**", "/orders/**").hasAuthority("ADMIN")
				.anyRequest().authenticated()
				.and()
				.oauth2Login()
				.loginPage("/user-login")
				.defaultSuccessUrl("/", true)
				.userInfoEndpoint()
				.userService(customOAuth2UserService)
				.and()
				.and()
				.formLogin()
				.loginPage("/admin-login")
				.loginProcessingUrl("/admin-login") // IMPORTANT!
				.defaultSuccessUrl("/book", true)
				.failureUrl("/admin-login?error")
				.and()
				.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/")
				.and()
				.headers()
				.frameOptions().disable();
	}


}
