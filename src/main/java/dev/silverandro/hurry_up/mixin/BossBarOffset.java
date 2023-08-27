package dev.silverandro.hurry_up.mixin;

import dev.silverandro.hurry_up.PlayerTimer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BossBarHud.class)
public class BossBarOffset {
	@ModifyConstant(method = "render", constant = {@Constant(intValue = 12)})
	public int hurry_up$offsetBossBar(int constant) {
		var player = (PlayerTimer)MinecraftClient.getInstance().player;
        if (player == null || player.hurry_up$isDefused()) {
			return constant;
		} else {
			return constant + MinecraftClient.getInstance().textRenderer.fontHeight + 2;
		}
	}
}
