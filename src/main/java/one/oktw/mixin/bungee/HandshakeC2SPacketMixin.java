package one.oktw.mixin.bungee;

import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(HandshakeC2SPacket.class)
public abstract class HandshakeC2SPacketMixin {
    @ModifyConstant(method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V", constant = @Constant(intValue = 255))
    private static int readStringSize(int i) {
        return Short.MAX_VALUE;
    }
}
