package one.oktw.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.util.UserCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(UserCache.class)
public class UserCacheMixin {
    @Redirect(method = "findProfileByName", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/UserCache;getOfflinePlayerProfile(Ljava/lang/String;)Ljava/util/Optional;"))
    private static Optional<GameProfile> getOfflinePlayerProfile(String string) {
        return Optional.empty();
    }
}
