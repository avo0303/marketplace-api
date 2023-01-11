package  com.andrewsha.marketplace.utils;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.RepresentationModel;

/**
 * ModelBuilder
 */
public interface DTOBuilder<T extends Object> {
	public RepresentationModel<?> build(T source);
	public RepresentationModel<?> build(Page<T> page);
}
