package one.oktw.mixin.bungee;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import one.oktw.interfaces.BungeeClientConnection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLoginNetworkHandler.class)
public abstract class ServerLoginNetworkHandlerMixin {

    @Shadow
    @Final
    ClientConnection connection;

    @Shadow
    private GameProfile profile;


    @Inject(method = "onHello", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerLoginNetworkHandler;startVerify(Lcom/mojang/authlib/GameProfile;)V",
            shift = At.Shift.BEFORE))
    private void initUuid(LoginHelloC2SPacket packet, CallbackInfo ci) {
        if (this.profile != null) {
            // override game profile with saved information:
            this.profile = new GameProfile(((BungeeClientConnection) connection).getSpoofedUUID(), this.profile.getName());

            if (((BungeeClientConnection) connection).getSpoofedProfile() != null) {
                for (Property property : ((BungeeClientConnection) connection).getSpoofedProfile()) {
                    this.profile.getProperties().put(property.name(), property);
                }
            }
        }
    }

    @Redirect(method = "onHello", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;isOnlineMode()Z"))
    private boolean skipKeyPacket(MinecraftServer minecraftServer) {
        return false;
    }
}
