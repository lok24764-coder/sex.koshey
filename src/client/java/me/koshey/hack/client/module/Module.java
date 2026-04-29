package me.koshey.hack.client.module;

import me.koshey.hack.client.setting.Setting;
import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled = false;
    private int keyBind = -1;
    private final List<Setting<?>> settings = new ArrayList<>();
    
    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }
    
    public void toggle() {
        setEnabled(!enabled);
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }
    
    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public int getKeyBind() {
        return keyBind;
    }
    
    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }
    
    public List<Setting<?>> getSettings() {
        return settings;
    }
    
    protected void addSetting(Setting<?> setting) {
        settings.add(setting);
    }
    
    public enum Category {
        COMBAT("Combat"),
        MOVEMENT("Movement"),
        RENDER("Render"),
        PLAYER("Player"),
        MISC("Misc"),
        OTHER("Other");
        
        private final String name;
        
        Category(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
    }
}
