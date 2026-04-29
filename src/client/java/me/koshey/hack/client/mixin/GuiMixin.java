package me.koshey.hack.client.mixin;

import me.koshey.hack.client.KosheyClient;
import me.koshey.hack.client.event.impl.RenderHUDEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.DeltaTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    public void onRenderHUD(GuiGraphicsExtractor drawContext, DeltaTracker deltaTracker, CallbackInfo ci) {
        RenderHUDEvent event = new RenderHUDEvent(drawContext);
        KosheyClient.EVENT_BUS.post(event);
    }
}
