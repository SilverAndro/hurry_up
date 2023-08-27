package dev.silverandro.hurry_up

import dev.silverandro.hurry_up.command.HurryUpCommand
import dev.silverandro.hurry_up.packet.SyncPacket
import dev.silverandro.hurry_up.util.*
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.world.GameMode
import net.minecraft.world.World
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.loader.api.config.QuiltConfig
import org.quiltmc.qkl.library.commands.onCommandRegistration
import org.quiltmc.qkl.library.lifecycle.onServerTickEnd
import org.quiltmc.qkl.library.networking.allPlayers
import org.quiltmc.qkl.library.registerEvents
import org.quiltmc.qkl.library.text.Color
import org.quiltmc.qkl.library.text.buildText
import org.quiltmc.qkl.library.text.color
import org.quiltmc.qkl.library.text.literal
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.networking.api.ServerPlayNetworking
import org.slf4j.LoggerFactory

object HurryUpMain : ModInitializer {
    val LOGGER = LoggerFactory.getLogger("Hurry Up!")

    val SYNC_ID = Identifier("hurry_up", "sync")
    val TIME_GAIN_ID = Identifier("hurry_up", "gain_time")

    val DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier("hurry_up", "out_of_time"))

    @JvmField
    val CONFIG = QuiltConfig.create(
        "hurry_up",
        "mechanics",
        HurryUpConfig::class.java
    )

    override fun onInitialize(mod: ModContainer) {
        LOGGER.info("Time waits for nobody!")

        registerEvents {
            onCommandRegistration { _, _ ->
                HurryUpCommand.register(this)
            }

            onServerTickEnd {
                if (saveProperties.dragonFightState.dragonKilled) {
                    allPlayers.forEach {
                        it.defused = true
                    }
                }

                allPlayers.forEach {
                    if (!it.defused) {
                        if (it.remainingTicks <= 0) {
                            timesUp(it)
                            it.defused = true
                            it.changeGameMode(GameMode.SPECTATOR)
                        } else {
                            it.remainingTicks--
                        }
                    }

                    if (it.remainingTicks > CONFIG.maxTicks) {
                        it.remainingTicks = CONFIG.maxTicks
                    }

                    if (it.lastDeathTime > 0) {
                        it.lastDeathTime--
                    }

                    val color = if (it.remainingTicks < MINUTE) {
                        Color.RED
                    } else {
                        Color.WHITE
                    }

                    if (ServerPlayNetworking.canSend(it, SYNC_ID)) {
                        val packet = SyncPacket(it.defused, it.remainingTicks, color.value)
                        ServerPlayNetworking.send(it, SYNC_ID, packet.toBuffer())
                    } else {
                        it.sendMessage(buildText {
                            color(color) {
                                literal(ticksToTime(it.remainingTicks))
                            }
                        }, true)
                    }
                }
            }
        }
    }

    private fun timesUp(player: ServerPlayerEntity) {
        if (CONFIG.explode) {
            player.world.createExplosion(player, player.x, player.y + 0.5, player.z, 2f, World.ExplosionSourceType.TNT)
        }

        player.kill()
    }

    fun onDeath(player: ServerPlayerEntity) {
        if (player.remainingTicks > 0 && player.lastDeathTime <= 0 && !player.server.isHardcore) {
            player.remainingTicks += CONFIG.deathBuffer
        }

        player.lastDeathTime = (CONFIG.deathBuffer * 2.1).toLong()
    }
}
