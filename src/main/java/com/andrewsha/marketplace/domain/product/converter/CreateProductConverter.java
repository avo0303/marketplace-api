package com.andrewsha.marketplace.domain.product.converter;

import com.andrewsha.marketplace.domain.product.request.CreateProductForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateProductConverter implements Converter<String, CreateProductForm> {
	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private Logger logger;

	@Override
	public CreateProductForm convert(String value) {
		try {
			return mapper.readValue(value, CreateProductForm.class);
		} catch (JsonMappingException e) {
			this.logger.error(e.getOriginalMessage());
		} catch (JsonProcessingException e) {
			this.logger.error(e.getOriginalMessage());
		}
		return null;
	}
}
