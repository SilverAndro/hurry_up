package dev.silverandro.hurry_up

import dev.silverandro.hurry_up.packet.SyncPacket
import dev.silverandro.hurry_up.util.defused
import dev.silverandro.hurry_up.util.remainingTicks
import dev.silverandro.hurry_up.util.ticksToTime
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qkl.library.text.Color
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking

object HurryUpClient : ClientModInitializer {
    var timerText = ""
    var timerColor = 0
    val plusTimeEvents = mutableListOf<PlusTimeEvent>()

    data class PlusTimeEvent(val ticks: Int, var displayTime: Int)

    override fun onInitializeClient(mod: ModContainer) {
        ClientPlayNetworking.registerGlobalReceiver(HurryUpMain.SYNC_ID) { client, _, buf, _ ->
            val packet = SyncPacket.fromBuffer(buf)
            client.player?.defused = packet.defused
            client.player?.remainingTicks = packet.remainingTicks
            timerColor = packet.color

            timerText = ticksToTime(packet.remainingTicks)
        }

        ClientPlayNetworking.registerGlobalReceiver(HurryUpMain.TIME_GAIN_ID) { client, _, buf, _ ->
            val ticks = buf.readVarInt()
            plusTimeEvents.add(PlusTimeEvent(ticks, 0))
        }

        HudRenderCallback.EVENT.register { graphics, delta ->
            if (MinecraftClient.getInstance().player?.defused == false) {
                val textRenderer = MinecraftClient.getInstance().textRenderer

                val textWidth = textRenderer.getWidth(timerText)
                val l = (graphics.scaledWindowWidth / 2) - (textWidth / 2) + 2

                graphics.fill(l - 2, -1, l + textWidth + 1, textRenderer.fontHeight + 2, -1873784752)
                graphics.drawText(
                    textRenderer,
                    timerText,
                    (graphics.scaledWindowWidth / 2) - (textRenderer.getWidth(timerText) / 2) + 2,
                    textRenderer.fontHeight / 4,
                    timerColor,
                    false
                )

                plusTimeEvents.forEach {
                    graphics.matrices.push()
                    val offsetUnit = 1 / 6.0
                    val offset = -(if (it.displayTime > 50) (it.displayTime - 50) * offsetUnit + (offsetUnit * delta) else 0.0)
                    val plusText = "+" + ticksToTime(it.ticks, false)
                    graphics.matrices.translate(0.0, 0.0 + offset, 0.0)
                    graphics.drawText(
                        textRenderer,
                        plusText,
                        (graphics.scaledWindowWidth / 2) - (textRenderer.getWidth(plusText) / 2) + 2,
                        textRenderer.fontHeight * 2,
                        Color.GREEN.value,
                        true
                    )
                    graphics.matrices.pop()
                }
            }
        }

        ClientTickEvents.END.register {
            plusTimeEvents.forEach {
                it.displayTime += 1
            }
            plusTimeEvents.removeIf { it.displayTime > 100 }
        }
    }
}
