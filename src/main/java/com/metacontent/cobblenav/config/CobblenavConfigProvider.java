package com.metacontent.cobblenav.config;

public class CobblenavConfigProvider implements SimpleConfig.DefaultConfig {
    private String content = "";

    public void addParameter(String key, Object value, String comment) {
        content += "#" + comment + " | default: " + value + "\n" + key + "=" + value + "\n";
    }

    @Override
    public String get(String namespace) {
        return content;
    }
}
