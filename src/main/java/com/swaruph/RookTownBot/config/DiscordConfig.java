package com.swaruph.RookTownBot.config;

public class DiscordConfig {

    public String getToken() {
        return ConfigLoader.getInstance().getProperty("BOT.TOKEN");
    }

}