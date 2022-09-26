package cc.woverflow.easeify.mixins;

import cc.woverflow.easeify.config.EaseifyConfig;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "gg.essential.handlers.OnlineIndicator")
public class EssentialIndicatorMixin {
    @Dynamic("Essential")
    @Inject(
            method = "getTextBackgroundOpacity", remap = false,
            at = @At("HEAD"),
            cancellable = true
    )
    private static void disableBackground(CallbackInfoReturnable<Integer> cir) {
        if (EaseifyConfig.INSTANCE.getRemoveNametagBackground()) {
            cir.setReturnValue(0);
        }
    }
}
