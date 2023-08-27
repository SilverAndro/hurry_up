package dev.silverandro.hurry_up.mixin;

import dev.silverandro.hurry_up.PlayerTimer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerDataPersistence {
    @Inject(method = "copyFrom", at = @At("RETURN"))
    public void hurry_up$copyTimerData(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        PlayerTimer thisTimer = (PlayerTimer) this;
        PlayerTimer oldTimer = (PlayerTimer) oldPlayer;

        thisTimer.hurry_up$setDefused(oldTimer.hurry_up$isDefused());
        thisTimer.hurry_up$setTicks(oldTimer.hurry_up$getRemainingTicks());
    }
}
