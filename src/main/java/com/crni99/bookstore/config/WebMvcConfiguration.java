package com.crni99.bookstore.config;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class WebMvcConfiguration {
	
	@Value("${spring.datasource.driver-class-name}")
	private String driver;

	@Value("${spring.datasource.url}")
	private String dbURL;

	@Value("${spring.datasource.username}")
	private String dbUsername;

	@Value("${spring.datasource.password}")
	private String dbPassword;

	// Note: Hikari doesn't have a separate "initialPoolSize". It's controlled by minimum-idle.
// You can remove this or map it to minimum-idle if needed.
	@Value("${spring.datasource.hikari.minimum-idle}")
	private int connPoolInitSize;

	@Value("${spring.datasource.hikari.minimum-idle}")
	private int connPoolMinSize;

	@Value("${spring.datasource.hikari.maximum-pool-size}")
	private int connPoolMaxSize;

	@Value("${spring.datasource.hikari.idle-timeout}")
	private int connPoolMaxIdleTime;

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.addBasenames("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public DataSource securityDataSource() {

		ComboPooledDataSource securityDataSource = new ComboPooledDataSource();

		try {
			securityDataSource.setDriverClass(driver);
		} 
		catch (PropertyVetoException exc) {
			throw new RuntimeException(exc);
		}
		securityDataSource.setJdbcUrl(dbURL);
		securityDataSource.setUser(dbUsername);
		securityDataSource.setPassword(dbPassword);

		securityDataSource.setInitialPoolSize(connPoolInitSize);
		securityDataSource.setMinPoolSize(connPoolMinSize);
		securityDataSource.setMaxPoolSize(connPoolMaxSize);
		securityDataSource.setMaxIdleTime(connPoolMaxIdleTime);

		return securityDataSource;
	}

}
