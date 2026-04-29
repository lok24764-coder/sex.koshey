package me.koshey.hack.client.setting;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, Boolean defaultValue) {
        super(name, defaultValue);
    }
    
    public void toggle() {
        setValue(!getValue());
    }
}
