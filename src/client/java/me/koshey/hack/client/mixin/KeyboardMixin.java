package me.koshey.hack.client.mixin;

import me.koshey.hack.client.KosheyClient;
import me.koshey.hack.client.event.impl.KeyPressEvent;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardMixin {
    @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
    private void onKeyPress(long window, int scancode, KeyEvent keyEvent, CallbackInfo ci) {
        KeyPressEvent event = new KeyPressEvent(keyEvent.key(), scancode, 1);
        KosheyClient.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
