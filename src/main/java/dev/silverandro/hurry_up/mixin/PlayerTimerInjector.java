package dev.silverandro.hurry_up.mixin;

import dev.silverandro.hurry_up.HurryUpMain;
import dev.silverandro.hurry_up.PlayerTimer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerTimerInjector implements PlayerTimer {
	@Unique
	private boolean hurry_up$isDefused = false;
	@Unique
	private int hurry_up$remainingTicks = HurryUpMain.CONFIG.startingTicks;
	@Unique
	private long hurry_up$lastDeathTime = 0;

	@Override
	public void hurry_up$setDefused(boolean isDefused) {
		this.hurry_up$isDefused = isDefused;
	}

	@Override
	public void hurry_up$setTicks(int count) {
		this.hurry_up$remainingTicks = count;
	}

	@Override
	public void hurry_up$setlastDeathTime(long time) {
		this.hurry_up$lastDeathTime = time;
	}

	@Override
	public boolean hurry_up$isDefused() {
		return hurry_up$isDefused;
	}

	@Override
	public int hurry_up$getRemainingTicks() {
		return hurry_up$remainingTicks;
	}

	@Override
	public long hurry_up$getLastDeathTime() {
		return hurry_up$lastDeathTime;
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void hurry_up$writeData(NbtCompound nbt, CallbackInfo ci) {
		NbtCompound data = new NbtCompound();
		data.putBoolean("defused", hurry_up$isDefused);
		data.putInt("remainingTicks", hurry_up$remainingTicks);
		nbt.put("hurry_up", data);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void hurry_up$readData(NbtCompound nbt, CallbackInfo ci) {
		if (nbt.contains("hurry_up")) {
			NbtCompound data = nbt.getCompound("hurry_up");
			hurry_up$isDefused = data.getBoolean("defused");
			hurry_up$remainingTicks = data.getInt("remainingTicks");
		} else {
			hurry_up$remainingTicks = HurryUpMain.CONFIG.startingTicks;
		}
	}
}
