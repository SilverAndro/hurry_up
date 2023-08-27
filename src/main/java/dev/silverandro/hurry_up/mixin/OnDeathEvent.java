package dev.silverandro.hurry_up.mixin;

import dev.silverandro.hurry_up.HurryUpMain;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class OnDeathEvent {
    @Inject(method = "onDeath", at = @At("TAIL"))
    public void hurry_up$onDeath(DamageSource source, CallbackInfo ci) {
        HurryUpMain.INSTANCE.onDeath((ServerPlayerEntity) (Object) this);
    }
}
