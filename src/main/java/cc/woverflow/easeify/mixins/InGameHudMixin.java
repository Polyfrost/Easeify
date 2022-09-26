package cc.woverflow.easeify.mixins;

import cc.woverflow.easeify.config.EaseifyConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.mojang.blaze3d.platform.GlStateManager;
import gg.essential.universal.UResolution;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow @Nullable private Text title;

    @Shadow @Nullable private Text subtitle;

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void isInContainer(MatrixStack matrices, CallbackInfo ci) {
        if (EaseifyConfig.INSTANCE.getDisableCrosshairGui() && MinecraftClient.getInstance().currentScreen != null) ci.cancel();
    }

    @ModifyExpressionValue(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/Perspective;isFirstPerson()Z"))
    private boolean shouldMakeRenderingPersistent(boolean initial) {
        return initial || EaseifyConfig.INSTANCE.getPersistentCrosshair();
    }

    @WrapWithCondition(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;)V"))
    private boolean makeTransparentOnlyIfAllowed(GlStateManager.SrcFactor srcFactor, GlStateManager.DstFactor dstFactor, GlStateManager.SrcFactor srcFactor2, GlStateManager.DstFactor dstFactor2) {
        return !EaseifyConfig.INSTANCE.getRemoveCrosshairTransparency();
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"))
    private int clamp(int value) {
        if (EaseifyConfig.INSTANCE.getDisableTitles()) {
            return 0;
        } else {
            return value;
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 0, shift = At.Shift.AFTER))
    private void modifyTitle(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        float titleScale = EaseifyConfig.INSTANCE.getTitleScale();
        if (EaseifyConfig.INSTANCE.getAutoTitleScale()) {
            final float width = MinecraftClient.getInstance().textRenderer.getWidth(title) * 4.0F;
            if (width > UResolution.getScaledWidth()) {
                titleScale = (UResolution.getScaledWidth() / width) * EaseifyConfig.INSTANCE.getTitleScale();
            }
        }
        matrices.scale(titleScale, titleScale, titleScale);
    }
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 1, shift = At.Shift.AFTER))
    private void modifySubtitle(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        float titleScale = EaseifyConfig.INSTANCE.getTitleScale();
        if (EaseifyConfig.INSTANCE.getAutoTitleScale()) {
            final float width = MinecraftClient.getInstance().textRenderer.getWidth(subtitle) * 2.0F;
            if (width > UResolution.getScaledWidth()) {
                titleScale = (UResolution.getScaledWidth() / width) * EaseifyConfig.INSTANCE.getTitleScale();
            }
        }
        matrices.scale(titleScale, titleScale, titleScale);
    }

    @ModifyConstant(method = "render", constant = @Constant(intValue = 255, ordinal = 3))
    private int modifyOpacity(int constant) {
        return (int) (EaseifyConfig.INSTANCE.getTitleOpacity() * 255);
    }
}
