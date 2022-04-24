package cc.woverflow.easeify.mixins;

import cc.woverflow.easeify.config.EaseifyConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void isInContainer(MatrixStack matrices, CallbackInfo ci) {
        if (EaseifyConfig.INSTANCE.getDisableCrosshairGui() && MinecraftClient.getInstance().currentScreen != null) ci.cancel();
    }
}
