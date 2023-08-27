package dev.silverandro.hurry_up.util

import dev.silverandro.hurry_up.PlayerTimer
import net.minecraft.entity.player.PlayerEntity

var PlayerEntity.defused: Boolean
    get() = (this as PlayerTimer).`hurry_up$isDefused`()
    set(value) = (this as PlayerTimer).`hurry_up$setDefused`(value)

var PlayerEntity.remainingTicks: Int
    get() = (this as PlayerTimer).`hurry_up$getRemainingTicks`()
    set(value) = (this as PlayerTimer).`hurry_up$setTicks`(value)

var PlayerEntity.lastDeathTime: Long
    get() = (this as PlayerTimer).`hurry_up$getLastDeathTime`()
    set(value) = (this as PlayerTimer).`hurry_up$setlastDeathTime`(value)
