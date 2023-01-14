package com.andrewsha.marketplace.domain.product_card.converter;

import com.andrewsha.marketplace.domain.product_card.request.CreateProductCardForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateFormConverter implements Converter<String, CreateProductCardForm> {
	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private Logger logger;

	@Override
	public CreateProductCardForm convert(String value) {
		try {
			return mapper.readValue(value, CreateProductCardForm.class);
		} catch (JsonMappingException e) {
			this.logger.error(e.getOriginalMessage());
		} catch (JsonProcessingException e) {
			this.logger.error(e.getOriginalMessage());
		}
		return null;
	}
}
