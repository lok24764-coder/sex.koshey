package me.koshey.hack.client.ui.clickgui;

import me.koshey.hack.client.KosheyClient;
import me.koshey.hack.client.module.Module;

import java.util.ArrayList;
import java.util.List;

public class CategoryFrame {
    private final Module.Category category;
    private int x, y;
    private final int width = 110;
    private int height = 20;
    private boolean dragging = false;
    private int dragX, dragY;
    private final List<ModuleButton> buttons = new ArrayList<>();
    
    public CategoryFrame(Module.Category category, int x, int y) {
        this.category = category;
        this.x = x;
        this.y = y;
        
        List<Module> modules = KosheyClient.INSTANCE.getModuleManager().getModulesByCategory(category);
        for (Module module : modules) {
            buttons.add(new ModuleButton(module, this));
        }
        
        updateHeight();
    }
    
    public void render(net.minecraft.client.gui.GuiGraphicsExtractor drawContext, int mouseX, int mouseY, float delta) {
        me.koshey.hack.client.render.RenderUtil.fill(drawContext, x, y, x + width, y + 20, 0xFF333333);
        me.koshey.hack.client.render.RenderUtil.drawString(drawContext, category.getName(), x + 5, y + 6, 0xFFFFFFFF);
        
        int offsetY = 20;
        for (ModuleButton button : buttons) {
            button.render(drawContext, mouseX, mouseY, x, y + offsetY, width);
            offsetY += button.getHeight();
        }
    }
    
    private void updateHeight() {
        height = 20;
        for (ModuleButton button : buttons) {
            height += button.getHeight();
        }
    }
    
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY, x, y, width, 20)) {
            if (button == 0) {
                dragging = true;
                dragX = (int) (mouseX - x);
                dragY = (int) (mouseY - y);
                return true;
            }
        }
        
        int offsetY = 20;
        for (ModuleButton moduleButton : buttons) {
            if (moduleButton.mouseClicked(mouseX, mouseY, button, x, y + offsetY, width)) {
                updateHeight();
                return true;
            }
            offsetY += moduleButton.getHeight();
        }
        
        return false;
    }
    
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            dragging = false;
        }
    }
    
    public void mouseDragged(double mouseX, double mouseY, double dragX, double dragY) {
        if (dragging) {
            this.x = (int) (mouseX - this.dragX);
            this.y = (int) (mouseY - this.dragY);
        }
    }
    
    public boolean keyPressed(int keyCode) {
        for (ModuleButton button : buttons) {
            if (button.keyPressed(keyCode)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isHovered(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
