package me.koshey.hack.client.module.impl;

import me.koshey.hack.client.module.Module;
import me.koshey.hack.client.setting.ModeSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class NoFallModule extends Module {
    private final ModeSetting mode;
    
    public NoFallModule() {
        super("NoFall", "Prevents fall damage", Category.PLAYER);
        
        mode = new ModeSetting("Mode", "Packet", "Packet", "Ground");
        addSetting(mode);
    }
    
    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        
        if (mc.player.fallDistance > 2.0f) {
            if (mode.is("Packet")) {
                mc.player.connection.send(new ServerboundMovePlayerPacket.StatusOnly(true, true));
            } else if (mode.is("Ground")) {
                mc.player.setOnGround(true);
            }
        }
    }
}
