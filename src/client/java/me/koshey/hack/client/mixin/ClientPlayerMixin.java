package me.koshey.hack.client.mixin;

import me.koshey.hack.client.KosheyClient;
import me.koshey.hack.client.module.impl.VelocityModule;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LocalPlayer.class)
public class ClientPlayerMixin {
}
