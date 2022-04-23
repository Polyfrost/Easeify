package cc.woverflow.easeify.mixins;

import cc.woverflow.easeify.hooks.NametagHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/client/font/TextRenderer$Drawer")
public class TextRendererDrawerMixin {
    @Shadow float x;

    @Inject(method = "drawLayer(IF)F", at = @At("HEAD"), cancellable = true)
    private void cancel(int underlineColor, float x, CallbackInfoReturnable<Float> cir) {
        if (NametagHook.INSTANCE.getShouldCancelBackground()) {
            cir.setReturnValue(this.x);
        }
    }
}
