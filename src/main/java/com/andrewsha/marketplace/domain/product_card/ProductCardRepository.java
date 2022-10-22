package com.andrewsha.marketplace.domain.product_card;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ProductCardRepository extends PagingAndSortingRepository<ProductCard, UUID> {
    Page<ProductCard> findByCategory(String category, Pageable pageable);

    @Query(value = "SELECT * FROM product_cards " + "LEFT OUTER JOIN product_card_products "
            + "ON product_cards.id = product_card_products.product_card_id "
            + "LEFT OUTER JOIN products " + "ON products.id = product_card_products.product_id "
            + "WHERE CAST(attributes as jsonb) @> CAST(:attributes as jsonb)",
            countQuery = "SELECT count(*) FROM product_cards", nativeQuery = true)
    Page<ProductCard> findByAttributes(@Param("attributes") String attributes, Pageable pageable);

    @Query(value = "SELECT * FROM product_cards " + "LEFT OUTER JOIN product_card_products "
            + "ON product_cards.id = product_card_products.product_card_id "
            + "LEFT OUTER JOIN products " + "ON products.id = product_card_products.product_id "
            + "WHERE CAST(attributes as jsonb) @> CAST(:attributes as jsonb) and category = :category",
            countQuery = "SELECT count(*) FROM product_cards", nativeQuery = true)
    Page<ProductCard> findByCategoryAndAttributes(@Param("category") String category,
            @Param("attributes") String attributes, Pageable pageable);

}
