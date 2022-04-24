package cc.woverflow.easeify.mixins;

import cc.woverflow.easeify.config.EaseifyConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At(value = "TAIL", shift = At.Shift.BEFORE), cancellable = true)
    private void renderPlayerNametag(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(
                (MinecraftClient.isHudEnabled() || EaseifyConfig.INSTANCE.getPersistentNametags()) && (EaseifyConfig.INSTANCE.getRenderOwnNametag() || livingEntity != MinecraftClient.getInstance().getCameraEntity()) && !livingEntity.isInvisibleTo(MinecraftClient.getInstance().player) && !livingEntity.hasPassengers()
        );
    }
}
