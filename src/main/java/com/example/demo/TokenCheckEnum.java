package com.example.demo;

public enum TokenCheckEnum {
    WS("ws"),
    LOCAL_KEY_FILE("local_key_file"),
    LOCAL_KEY_PLAIN("local_key"),
    REMOTE_KEY("remote_key");

    TokenCheckEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
