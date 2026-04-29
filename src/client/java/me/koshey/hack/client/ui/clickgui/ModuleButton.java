package me.koshey.hack.client.ui.clickgui;

import me.koshey.hack.client.KosheyClient;
import me.koshey.hack.client.font.CustomFont;
import me.koshey.hack.client.module.Module;
import me.koshey.hack.client.setting.BooleanSetting;
import me.koshey.hack.client.setting.ModeSetting;
import me.koshey.hack.client.setting.NumberSetting;
import me.koshey.hack.client.setting.Setting;

public class ModuleButton {
    private final Module module;
    private final CategoryFrame parent;
    private boolean expanded = false;
    private boolean binding = false;
    private static final int MODULE_HEIGHT = 16;
    private static final int SETTING_HEIGHT = 14;
    
    public ModuleButton(Module module, CategoryFrame parent) {
        this.module = module;
        this.parent = parent;
    }
    
    public void render(net.minecraft.client.gui.GuiGraphicsExtractor drawContext, int mouseX, int mouseY, int x, int y, int width) {
        me.koshey.hack.client.module.impl.ColorsModule colors = (me.koshey.hack.client.module.impl.ColorsModule) KosheyClient.INSTANCE.getModuleManager().getModuleByName("Colors");
        int globalColor = colors != null ? colors.getColor() : 0xFF44AA44;
        
        int bgColor = module.isEnabled() ? globalColor : 0xFF222222;
        me.koshey.hack.client.render.RenderUtil.fill(drawContext, x, y, x + width, y + MODULE_HEIGHT, bgColor);
        me.koshey.hack.client.render.RenderUtil.drawString(drawContext, module.getName(), x + 5, y + 4, 0xFFFFFFFF);
        
        if (expanded) {
            int offsetY = MODULE_HEIGHT;
            for (Setting<?> setting : module.getSettings()) {
                me.koshey.hack.client.render.RenderUtil.fill(drawContext, x, y + offsetY, x + width, y + offsetY + SETTING_HEIGHT, 0xFF111111);
                
                String text = setting.getName();
                if (setting instanceof BooleanSetting bool) {
                    text += ": " + (bool.getValue() ? "On" : "Off");
                } else if (setting instanceof NumberSetting num) {
                    text += ": " + String.format("%.1f", num.getValue());
                } else if (setting instanceof ModeSetting mode) {
                    text += ": " + mode.getValue();
                }
                
                me.koshey.hack.client.render.RenderUtil.drawString(drawContext, text, x + 10, y + offsetY + 3, 0xFFAAAAAA);
                offsetY += SETTING_HEIGHT;
            }
            
            // Render Bind button
            me.koshey.hack.client.render.RenderUtil.fill(drawContext, x, y + offsetY, x + width, y + offsetY + SETTING_HEIGHT, 0xFF151515);
            String bindText = binding ? "Binding..." : "Bind: " + (module.getKeyBind() == -1 ? "None" : org.lwjgl.glfw.GLFW.glfwGetKeyName(module.getKeyBind(), 0) != null ? org.lwjgl.glfw.GLFW.glfwGetKeyName(module.getKeyBind(), 0).toUpperCase() : String.valueOf(module.getKeyBind()));
            me.koshey.hack.client.render.RenderUtil.drawString(drawContext, bindText, x + 10, y + offsetY + 3, 0xFFAAAAAA);
        }
    }
    
    public boolean mouseClicked(double mouseX, double mouseY, int button, int x, int y, int width) {
        if (isHovered(mouseX, mouseY, x, y, width, MODULE_HEIGHT)) {
            if (button == 0) {
                module.toggle();
                return true;
            } else if (button == 1) {
                expanded = !expanded;
                return true;
            }
        }
        
        if (expanded) {
            int offsetY = MODULE_HEIGHT;
            for (Setting<?> setting : module.getSettings()) {
                if (isHovered(mouseX, mouseY, x, y + offsetY, width, SETTING_HEIGHT)) {
                    if (button == 0) {
                        if (setting instanceof BooleanSetting bool) {
                            bool.toggle();
                        } else if (setting instanceof ModeSetting mode) {
                            mode.cycle();
                        }
                        return true;
                    }
                }
                offsetY += SETTING_HEIGHT;
            }
            
            if (isHovered(mouseX, mouseY, x, y + offsetY, width, SETTING_HEIGHT)) {
                if (button == 0) {
                    binding = !binding;
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public boolean keyPressed(int keyCode) {
        if (binding) {
            if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_DELETE || keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE || keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE) {
                module.setKeyBind(-1);
            } else {
                module.setKeyBind(keyCode);
            }
            binding = false;
            return true;
        }
        return false;
    }
    
    public int getHeight() {
        int height = MODULE_HEIGHT;
        if (expanded) {
            height += module.getSettings().size() * SETTING_HEIGHT + SETTING_HEIGHT; // +1 for Bind button
        }
        return height;
    }
    
    private boolean isHovered(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
