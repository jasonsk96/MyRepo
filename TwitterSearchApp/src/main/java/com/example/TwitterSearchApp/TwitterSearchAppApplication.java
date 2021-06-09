package com.example.TwitterSearchApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.example.TwitterSearchApp.OntService.*;

@SpringBootApplication
public class TwitterSearchAppApplication {

	// Register Servlet
	  @Bean
	  public ServletRegistrationBean servletRegistrationBean() {
	    ServletRegistrationBean bean = new ServletRegistrationBean(
	        new OntServlet(), "/OntServlet");
	    return bean;
	  }

	
	public static void main(String[] args) {
		SpringApplication.run(TwitterSearchAppApplication.class, args);
	}

}
