package com.bootmongo.config;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "com.bootmongo")
@EnableMongoRepositories
@EnableAutoConfiguration
@SpringBootApplication
public class AppConfig {

	public static void main(String[] args) {
		SpringApplication.run(AppConfig.class, args);
	}

	@Bean(name = "org.dozer.Mapper")
	public Mapper mapper() {
		final DozerBeanMapper dozerBean = new DozerBeanMapper();
		List<String> location = new ArrayList<String>();
		location.add("mapping-file.xml");
		dozerBean.setMappingFiles(location);
		return dozerBean;
	}
}
