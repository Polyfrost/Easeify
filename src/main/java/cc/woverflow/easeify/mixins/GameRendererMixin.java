package cc.woverflow.easeify.mixins;

import cc.woverflow.easeify.config.EaseifyConfig;
import cc.woverflow.easeify.hooks.FOVMultiplierHook;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyExpressionValue(method = "updateFovMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getFovMultiplier()F"))
    private float modifyMultiplier(float initial) {
        return FOVMultiplierHook.INSTANCE.modifyFOVMultiplier(initial);
    }

    @Redirect(method = "renderWorld", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;bobView:Z", opcode = Opcodes.GETFIELD))
    private boolean redirectWorldBobView(GameOptions instance) {
        return !EaseifyConfig.INSTANCE.getRemoveScreenBobbing() && instance.bobView;
    }

    @Redirect(method = "renderHand", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;bobView:Z", opcode = Opcodes.GETFIELD))
    private boolean redirectHandBobView(GameOptions instance) {
        if (EaseifyConfig.INSTANCE.getRemoveMapBobbing()) {
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
                    if (offHandStack.getItem() instanceof FilledMapItem) {
                        return false;
                    }
                }
            }
        }
        return instance.bobView;
    }
}
