package me.koshey.hack.client;

import me.koshey.hack.client.event.EventBus;
import me.koshey.hack.client.event.Listener;
import me.koshey.hack.client.event.impl.KeyPressEvent;
import me.koshey.hack.client.event.impl.MouseClickEvent;
import me.koshey.hack.client.event.impl.RenderHUDEvent;
import me.koshey.hack.client.font.FontManager;
import me.koshey.hack.client.module.Module;
import me.koshey.hack.client.module.ModuleManager;
import me.koshey.hack.client.ui.clickgui.ClickGUI;
import me.koshey.hack.client.ui.hud.HUDManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class KosheyClient implements ClientModInitializer, Listener {
    public static KosheyClient INSTANCE;
    public static final EventBus EVENT_BUS = new EventBus();
    
    public FontManager fontManager;
    private ModuleManager moduleManager;
    private ClickGUI clickGUI;
    private HUDManager hudManager;
    
    private KeyMapping clickGuiKey;
    private boolean middleMousePressed = false;
    
    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        me.koshey.hack.client.test.TestLoc.main(null);
        
        this.fontManager = new FontManager();
        this.moduleManager = new ModuleManager();
        this.clickGUI = new ClickGUI();
        this.hudManager = new HUDManager();
        
        EVENT_BUS.register(this);
        EVENT_BUS.register(hudManager);
        
        clickGuiKey = new KeyMapping(
            "key.koshey.clickgui",
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            KeyMapping.Category.MISC
        );
        
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }
    
    private void onClientTick(Minecraft client) {
        if (client.player == null) return;
        
        while (clickGuiKey.consumeClick()) {
            if (client.screen instanceof ClickGUI) {
                client.setScreen(null);
            } else if (client.screen == null) {
                client.setScreen(clickGUI);
            }
        }
        
        moduleManager.onTick();
    }
    
    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getKey() == GLFW.GLFW_KEY_UNKNOWN) return;
        
        for (Module module : moduleManager.getModules()) {
            if (module.getKeyBind() == event.getKey()) {
                module.toggle();
            }
        }
    }
    
    public FontManager getFontManager() {
        return fontManager;
    }
    
    public ModuleManager getModuleManager() {
        return moduleManager;
    }
    
    public ClickGUI getClickGUI() {
        return clickGUI;
    }
    
    public HUDManager getHudManager() {
        return hudManager;
    }
}
