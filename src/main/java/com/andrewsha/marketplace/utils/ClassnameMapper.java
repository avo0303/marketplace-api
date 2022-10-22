package com.andrewsha.marketplace.utils;

import java.util.HashMap;
import java.util.Map;

import com.andrewsha.marketplace.domain.product.Product;
import com.andrewsha.marketplace.domain.product_card.ProductCard;
import com.andrewsha.marketplace.domain.store.Store;
import com.andrewsha.marketplace.domain.user.User;

public class ClassnameMapper {
    private static Map<String, Class<?>> classnameMap = new HashMap<String, Class<?>>();
    static {
        classnameMap.put("ProductCard", ProductCard.class);
        classnameMap.put("Store", Store.class);
        classnameMap.put("Product", Product.class);
        classnameMap.put("User", User.class);
    }

    public static Class<?> getClass(String className) {
        return classnameMap.get(className);
    }
}
