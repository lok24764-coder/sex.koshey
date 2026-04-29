package me.koshey.hack.client.module.impl;

import me.koshey.hack.client.module.Module;
import me.koshey.hack.client.setting.NumberSetting;

public class ColorsModule extends Module {
    public final NumberSetting red;
    public final NumberSetting green;
    public final NumberSetting blue;
    public final NumberSetting alpha;

    public ColorsModule() {
        super("Colors", "Global client color", Category.OTHER);

        red = new NumberSetting("Red", 255.0, 0.0, 255.0, 1.0);
        green = new NumberSetting("Green", 50.0, 0.0, 255.0, 1.0);
        blue = new NumberSetting("Blue", 50.0, 0.0, 255.0, 1.0);
        alpha = new NumberSetting("Alpha", 255.0, 0.0, 255.0, 1.0);

        addSetting(red);
        addSetting(green);
        addSetting(blue);
        addSetting(alpha);
        
        setEnabled(true);
    }

    public int getColor() {
        int r = (int) (double) red.getValue();
        int g = (int) (double) green.getValue();
        int b = (int) (double) blue.getValue();
        int a = (int) (double) alpha.getValue();
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}