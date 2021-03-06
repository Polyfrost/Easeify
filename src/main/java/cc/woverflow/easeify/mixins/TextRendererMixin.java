package cc.woverflow.easeify.mixins;

import cc.woverflow.easeify.config.EaseifyConfig;
import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TextRenderer.class)
public class TextRendererMixin {
    @ModifyVariable(method = "drawInternal(Ljava/lang/String;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZIIZ)I", at = @At("HEAD"), index = 5, argsOnly = true)
    private boolean disableShadow(boolean textShadow) {
        return (!EaseifyConfig.INSTANCE.getDisableTextShadow() && textShadow);
    }

    @ModifyVariable(method = "drawInternal(Lnet/minecraft/text/OrderedText;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)I", at = @At("HEAD"), index = 5, argsOnly = true)
    private boolean disableShadow2(boolean textShadow) {
        return (!EaseifyConfig.INSTANCE.getDisableTextShadow() && textShadow);
    }
}
