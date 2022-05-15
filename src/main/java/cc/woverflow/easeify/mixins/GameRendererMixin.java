package cc.woverflow.easeify.mixins;

import cc.woverflow.easeify.config.EaseifyConfig;
import cc.woverflow.easeify.hooks.BehindYouHook;
import cc.woverflow.easeify.hooks.FOVMultiplierHook;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void handleBehindYou(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        BehindYouHook.INSTANCE.onTick();
    }

    @ModifyExpressionValue(method = "updateFovMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getFovMultiplier()F"))
    private float modifyMultiplier(float initial) {
        return FOVMultiplierHook.INSTANCE.modifyFOVMultiplier(initial);
    }

    @WrapWithCondition(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
    private boolean shouldBobWorld(GameRenderer instance, MatrixStack matrices, float tickDelta) {
        return !EaseifyConfig.INSTANCE.getRemoveScreenBobbing();
    }

    @WrapWithCondition(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
    private boolean shouldBobHand(GameRenderer instance, MatrixStack matrices, float tickDelta) {
        if (!EaseifyConfig.INSTANCE.getRemoveMapBobbing()) {
            return true;
        }
        ClientPlayerEntity entity = MinecraftClient.getInstance().player;
        if (entity != null) {
            ItemStack mainStack = entity.getMainHandStack();
            ItemStack offHandStack = entity.getOffHandStack();
            if (mainStack != null) {
                if (mainStack.getItem() instanceof FilledMapItem) {
                    return false;
                }
            }
            if (offHandStack != null) {
                return !(offHandStack.getItem() instanceof FilledMapItem);
            }
        }
        return true;
    }
}
