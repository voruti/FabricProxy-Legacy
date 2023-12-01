package one.oktw.mixin.bungee;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import one.oktw.interfaces.BungeeClientConnection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerLoginNetworkHandler.class)
public abstract class ServerLoginNetworkHandlerMixin {

    @Shadow
    @Final
    ClientConnection connection;


    @Redirect(method = "onHello", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;getHostProfile()Lcom/mojang/authlib/GameProfile;"))
    private GameProfile initUuid(MinecraftServer minecraftServer) {
        final GameProfile originalGameProfile = minecraftServer.getHostProfile();

        // override game profile with saved information:
        final GameProfile gameProfile = new GameProfile(((BungeeClientConnection) connection).getSpoofedUUID(), originalGameProfile.getName());

        if (((BungeeClientConnection) connection).getSpoofedProfile() != null) {
            for (Property property : ((BungeeClientConnection) connection).getSpoofedProfile()) {
                gameProfile.getProperties().put(property.name(), property);
            }
        }

        return gameProfile;
    }

    @Redirect(method = "onHello", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;isOnlineMode()Z"))
    private boolean skipKeyPacket(MinecraftServer minecraftServer) {
        return false;
    }
}
