package com.andrewsha.marketplace.security;

import java.io.Serializable;
import java.util.Map;

public interface IBinaryTree {
    public Serializable getId();

    public Map.Entry<String, Serializable> getRoot();

    public Map.Entry<String, Serializable> getParent();
}
