package com.spring.SpringInAction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
//@SpringBootConfiguration
//@Configuration
//@EnableAutoConfiguration
//@ComponentScan(excludeFilters = { @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
//		@ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public class SpringInActionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringInActionApplication.class, args);
	}

}
