package com.andrewsha.marketplace.security;

public class JwtToken {
    private final String name;
    private final String value;

    public JwtToken(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    };
}
