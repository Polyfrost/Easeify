package cc.woverflow.easeify.mixins;

import cc.woverflow.easeify.config.EaseifyConfig;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntityRenderer.class)
public class ItemEntityRendererMixin {
    @Redirect(method = "render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;sin(F)F"))
    private float staticItems(float value) {
        return EaseifyConfig.INSTANCE.getStaticItems() ? -1.0f : value;
    }

    @Inject(method = "getRenderedAmount", at = @At("HEAD"), cancellable = true)
    private void forceAmount(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (EaseifyConfig.INSTANCE.getUnstackedItems()) cir.setReturnValue(1);
    }
}
