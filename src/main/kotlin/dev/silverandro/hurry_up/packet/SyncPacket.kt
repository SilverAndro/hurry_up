package dev.silverandro.hurry_up.packet

import net.minecraft.network.PacketByteBuf
import org.quiltmc.qsl.networking.api.PacketByteBufs

data class SyncPacket(val defused: Boolean, val remainingTicks: Int, val color: Int) {
    fun toBuffer(): PacketByteBuf {
        val buffer = PacketByteBufs.create()
        buffer.writeBoolean(defused)
        buffer.writeVarInt(remainingTicks)
        buffer.writeInt(color)
        return buffer
    }

    companion object {
        fun fromBuffer(buf: PacketByteBuf): SyncPacket {
            return SyncPacket(buf.readBoolean(), buf.readVarInt(), buf.readInt())
        }
    }
}
