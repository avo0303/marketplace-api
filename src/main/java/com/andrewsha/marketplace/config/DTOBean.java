package com.andrewsha.marketplace.config;

import com.andrewsha.marketplace.domain.product_card.ProductCard;
import com.andrewsha.marketplace.domain.product_card.resource.ProductCardDTOBuilder;
import com.andrewsha.marketplace.domain.user.User;
import com.andrewsha.marketplace.domain.user.resource.UserDTOBuilder;
import com.andrewsha.marketplace.utils.DTOBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DTOBean {

	@Bean
	public DTOBuilder<ProductCard> productCardDTOBuilder() {
		return new ProductCardDTOBuilder();
	}

	@Bean
	public DTOBuilder<User> userDTOBuilder() {
		return new UserDTOBuilder();
	}
}
