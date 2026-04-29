package me.koshey.hack.client.ui.hud;

import me.koshey.hack.client.event.Listener;
import me.koshey.hack.client.event.impl.RenderHUDEvent;
import me.koshey.hack.client.ui.hud.impl.KeybindsHUD;
import me.koshey.hack.client.ui.hud.impl.TargetHUD;
import me.koshey.hack.client.ui.hud.impl.WatermarkHUD;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

public class HUDManager implements Listener {
    private final List<HUDElement> elements = new ArrayList<>();
    private final File configFile = new File(net.minecraft.client.Minecraft.getInstance().gameDirectory, "koshey_hud.properties");
    
    public HUDManager() {
        elements.add(new WatermarkHUD());
        elements.add(new TargetHUD());
        elements.add(new KeybindsHUD());
        loadConfig();
    }
    
    public void loadConfig() {
        if (!configFile.exists()) return;
        try (FileReader reader = new FileReader(configFile)) {
            Properties props = new Properties();
            props.load(reader);
            for (HUDElement element : elements) {
                if (props.containsKey(element.getName() + "_X")) {
                    element.setX(Integer.parseInt(props.getProperty(element.getName() + "_X")));
                }
                if (props.containsKey(element.getName() + "_Y")) {
                    element.setY(Integer.parseInt(props.getProperty(element.getName() + "_Y")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void saveConfig() {
        try (FileWriter writer = new FileWriter(configFile)) {
            Properties props = new Properties();
            for (HUDElement element : elements) {
                props.setProperty(element.getName() + "_X", String.valueOf(element.getX()));
                props.setProperty(element.getName() + "_Y", String.valueOf(element.getY()));
            }
            props.store(writer, "Koshey Hack HUD Config");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onRenderHUD(RenderHUDEvent event) {
        if (net.minecraft.client.Minecraft.getInstance().screen instanceof me.koshey.hack.client.ui.hud.HudEditorScreen) return;
        
        for (HUDElement element : elements) {
            if (element.isEnabled()) {
                element.render(event.getDrawContext(), 1.0f);
            }
        }
    }
    
    public List<HUDElement> getElements() {
        return elements;
    }
}
