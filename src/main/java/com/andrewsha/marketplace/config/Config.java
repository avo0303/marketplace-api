package com.andrewsha.marketplace.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableCaching
public class Config implements WebMvcConfigurer {
	public static final String emailRegexp = "\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.\\w{2,4}";
	public static final String phoneNumberRegexp =
			"^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";
	public static final String passwordRegexp = "^[a-zA-Z]\\w{3,14}$";
	public static final String nameRegexp = "[A-Z][a-z]+( [A-Z][a-z]+)?";
	public static final String imageUrlRegexp =
			"^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

	@Value("${upload.path.products}")
	private String uploadPath;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/storage/products/**")
				.addResourceLocations("file://" + this.uploadPath + "/");
	}

	@Bean
	Logger getLogger() {
		return LoggerFactory.getLogger(this.getClass());
	}
}
