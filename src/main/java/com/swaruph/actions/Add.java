package com.swaruph.actions;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Add {
    public static String add(OptionMapping a, OptionMapping b) {
        return String.valueOf(a.getAsInt() + b.getAsInt());
    }
}