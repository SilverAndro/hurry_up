package dev.silverandro.hurry_up.mixin;

import dev.silverandro.hurry_up.HurryUpMain;
import dev.silverandro.hurry_up.PlayerTimer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageTracker.class)
public abstract class DeathMessageReplacer {
    @Shadow public abstract void onDamage(DamageSource damageSource, float originalHealth);

    @Shadow @Final private LivingEntity entity;

    @Inject(method = "getDeathMessage", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", ordinal = 0, shift = At.Shift.BEFORE))
    public void hurry_up$replaceDeathMessage(CallbackInfoReturnable<Text> cir) {
        if (entity instanceof ServerPlayerEntity spe && !((PlayerTimer)spe).hurry_up$isDefused() && ((PlayerTimer)spe).hurry_up$getRemainingTicks() <= 0) {
            DamageSource damage = entity.getWorld().getDamageSources().create(HurryUpMain.INSTANCE.getDAMAGE_TYPE(), null);
            onDamage(damage, Float.MAX_VALUE);
        }
    }
}
