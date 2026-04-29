package me.koshey.hack.client.ui.clickgui;

import me.koshey.hack.client.KosheyClient;
import me.koshey.hack.client.module.Module;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Screen {
    private final List<CategoryFrame> frames = new ArrayList<>();
    
    public ClickGUI() {
        super(Component.literal("ClickGUI"));
        
        int x = 10;
        for (Module.Category category : Module.Category.values()) {
            frames.add(new CategoryFrame(category, x, 10));
            x += 120;
        }
    }
    
    @Override
    public void extractRenderState(GuiGraphicsExtractor drawContext, int mouseX, int mouseY, float deltaTracker) {
        super.extractRenderState(drawContext, mouseX, mouseY, deltaTracker);
        
        for (CategoryFrame frame : frames) {
            frame.render(drawContext, mouseX, mouseY, deltaTracker);
        }
    }
    
    @Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean isDouble) {
        for (CategoryFrame frame : frames) {
            if (frame.mouseClicked(event.x(), event.y(), event.button())) {
                return true;
            }
        }
        return super.mouseClicked(event, isDouble);
    }
    
    @Override
    public boolean mouseReleased(net.minecraft.client.input.MouseButtonEvent event) {
        for (CategoryFrame frame : frames) {
            frame.mouseReleased(event.x(), event.y(), event.button());
        }
        return super.mouseReleased(event);
    }
    
    @Override
    public boolean mouseDragged(net.minecraft.client.input.MouseButtonEvent event, double dragX, double dragY) {
        for (CategoryFrame frame : frames) {
            frame.mouseDragged(event.x(), event.y(), dragX, dragY);
        }
        return super.mouseDragged(event, dragX, dragY);
    }
    
    @Override
    public boolean keyPressed(net.minecraft.client.input.KeyEvent event) {
        if (event.key() == org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT) {
            this.onClose();
            return true;
        }
        for (CategoryFrame frame : frames) {
            if (frame.keyPressed(event.key())) {
                return true;
            }
        }
        return super.keyPressed(event);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(null);
        }
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
