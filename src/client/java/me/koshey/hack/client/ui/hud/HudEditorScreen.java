package me.koshey.hack.client.ui.hud;

import me.koshey.hack.client.KosheyClient;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class HudEditorScreen extends Screen {
    private HUDElement draggingElement = null;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public HudEditorScreen() {
        super(Component.literal("HUD Editor"));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor drawContext, int mouseX, int mouseY, float deltaTracker) {
        super.extractRenderState(drawContext, mouseX, mouseY, deltaTracker);
        
        for (HUDElement element : KosheyClient.INSTANCE.getHudManager().getElements()) {
            if (element.isEnabled()) {
                element.render(drawContext, deltaTracker);
                // Draw outline for editor
                me.koshey.hack.client.render.RenderUtil.fill(drawContext, element.getX(), element.getY(), element.getX() + element.getWidth(), element.getY() + 1, 0xAAFFFFFF);
                me.koshey.hack.client.render.RenderUtil.fill(drawContext, element.getX(), element.getY() + element.getHeight() - 1, element.getX() + element.getWidth(), element.getY() + element.getHeight(), 0xAAFFFFFF);
                me.koshey.hack.client.render.RenderUtil.fill(drawContext, element.getX(), element.getY(), element.getX() + 1, element.getY() + element.getHeight(), 0xAAFFFFFF);
                me.koshey.hack.client.render.RenderUtil.fill(drawContext, element.getX() + element.getWidth() - 1, element.getY(), element.getX() + element.getWidth(), element.getY() + element.getHeight(), 0xAAFFFFFF);
            }
        }
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean isDouble) {
        for (HUDElement element : KosheyClient.INSTANCE.getHudManager().getElements()) {
            if (element.isEnabled() && isHovered(event.x(), event.y(), element)) {
                draggingElement = element;
                dragOffsetX = (int) (event.x() - element.getX());
                dragOffsetY = (int) (event.y() - element.getY());
                return true;
            }
        }
        return super.mouseClicked(event, isDouble);
    }

    @Override
    public boolean mouseReleased(net.minecraft.client.input.MouseButtonEvent event) {
        if (draggingElement != null) {
            KosheyClient.INSTANCE.getHudManager().saveConfig();
            draggingElement = null;
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(net.minecraft.client.input.MouseButtonEvent event, double dragX, double dragY) {
        if (draggingElement != null) {
            draggingElement.setX((int) (event.x() - dragOffsetX));
            draggingElement.setY((int) (event.y() - dragOffsetY));
            return true;
        }
        return super.mouseDragged(event, dragX, dragY);
    }
    
    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(null);
        }
    }

    private boolean isHovered(double mouseX, double mouseY, HUDElement element) {
        return mouseX >= element.getX() && mouseX <= element.getX() + element.getWidth() &&
               mouseY >= element.getY() && mouseY <= element.getY() + element.getHeight();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}