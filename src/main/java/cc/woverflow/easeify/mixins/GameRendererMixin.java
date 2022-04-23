package cc.woverflow.easeify.mixins;

import cc.woverflow.easeify.hooks.FOVMultiplierHook;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyExpressionValue(method = "updateFovMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getFovMultiplier()F"))
    private float modifyMultiplier(float initial) {
        return FOVMultiplierHook.INSTANCE.modifyFOVMultiplier(initial);
    }
}
