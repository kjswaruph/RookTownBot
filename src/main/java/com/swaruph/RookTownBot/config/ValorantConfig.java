package com.swaruph.RookTownBot.config;

public class ValorantConfig {

    public String getToken() {
        return ConfigLoader.getInstance().getProperty("HENRIKDEV_API_KEY");
    }

}
