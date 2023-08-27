package dev.silverandro.hurry_up.mixin;

import dev.silverandro.hurry_up.HurryUpMain;
import dev.silverandro.hurry_up.PlayerTimer;
import dev.silverandro.hurry_up.util.TimeFormatKt;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public class GrantTimeOnAdvancement {
	@Shadow
	private ServerPlayerEntity owner;

	@Inject(method = "grantCriterion", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/AdvancementRewards;apply(Lnet/minecraft/server/network/ServerPlayerEntity;)V"))
	public void onGrantAdvancement(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
		if (advancement.getDisplay() == null) return;

		int timeToGrant = HurryUpMain.CONFIG.advancementTicks;
		int currentTicks = ((PlayerTimer) owner).hurry_up$getRemainingTicks();
		((PlayerTimer) owner).hurry_up$setTicks(currentTicks + timeToGrant);

		if (ServerPlayNetworking.canSend(owner, HurryUpMain.INSTANCE.getTIME_GAIN_ID())) {
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeVarInt(timeToGrant);
			ServerPlayNetworking.send(owner, HurryUpMain.INSTANCE.getTIME_GAIN_ID(), buf);
		} else {
			owner.sendMessage(Text.literal("+" + TimeFormatKt.ticksToTime(timeToGrant, false)).formatted(Formatting.GREEN), false);
		}
	}
}
