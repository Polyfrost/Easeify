package cc.woverflow.easeify.mixins;

import cc.woverflow.easeify.config.EaseifyConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.entity.effect.StatusEffects.FIRE_RESISTANCE;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
    private static boolean hasPushed = false;

    @Inject(method = "renderUnderwaterOverlay", at = @At("HEAD"), cancellable = true)
    private static void cancelUnderwaterOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (EaseifyConfig.INSTANCE.getRemoveWaterOverlay()) ci.cancel();
    }

    @Inject(method = "renderFireOverlay", at = @At("HEAD"))
    private static void moveFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        hasPushed = true;
        matrices.push();
        matrices.translate(0, EaseifyConfig.INSTANCE.getFireOverlayHeight(), 0);
    }

    @Inject(method = "renderFireOverlay", at = @At("RETURN"))
    private static void resetFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (hasPushed) {
            hasPushed = false;
            matrices.pop();
        }
    }

    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private static void cancelFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (EaseifyConfig.INSTANCE.getRemoveFireOverlay() && client.player != null && (client.player.isCreative() || client.player.hasStatusEffect(FIRE_RESISTANCE))) ci.cancel();
    }
}
