package dev.silverandro.hurry_up.command

import com.mojang.brigadier.CommandDispatcher
import dev.silverandro.hurry_up.util.defused
import dev.silverandro.hurry_up.util.remainingTicks
import net.minecraft.server.command.ServerCommandSource
import org.quiltmc.qkl.library.brigadier.*
import org.quiltmc.qkl.library.brigadier.argument.*
import org.quiltmc.qkl.library.brigadier.util.required

object HurryUpCommand {
    fun register(dispatch: CommandDispatcher<ServerCommandSource>) {
        dispatch.register("hurry_up") {
            required(player("target")) { getPlayer ->
                required(literal("set")) {
                    required(literal("defused"), boolean("defused")) { _, newValue ->
                        execute {
                            val target = getPlayer().value()
                            val value = newValue().value()
                            target.defused = value
                        }
                    }
                    required(literal("remaining_ticks"), integer("remaining_ticks", min = 0)) { _, newValue ->
                        execute {
                            val target = getPlayer().value()
                            val value = newValue().value()
                            target.remainingTicks = value
                        }
                    }
                }
                required(literal("get"), enum("value", OperationTarget::class)) { _, getValue ->
                    executeWithResult {
                        val target = getPlayer().value()
                        val value = getValue().value()
                        val result = when (value) {
                            OperationTarget.TICKS_REMAINING -> CommandResult.success(target.remainingTicks)
                            OperationTarget.DEFUSED -> CommandResult.success(if (target.defused) 1 else 0)
                        }

                        return@executeWithResult result
                    }
                }
            }
        }
    }

    enum class OperationTarget {
        TICKS_REMAINING,
        DEFUSED
    }
}
