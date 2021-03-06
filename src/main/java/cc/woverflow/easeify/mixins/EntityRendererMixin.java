package cc.woverflow.easeify.mixins;

import cc.woverflow.easeify.config.EaseifyConfig;
import cc.woverflow.easeify.hooks.NametagHook;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"))
    private void setNameTagBackgroundCancel(Entity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        NametagHook.INSTANCE.setShouldCancelBackground(EaseifyConfig.INSTANCE.getRemoveNametagBackground());
    }

    @ModifyArgs(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)I"))
    private void setNametagShadow(Args args) {
        args.set(4, EaseifyConfig.INSTANCE.getNametagTextShadow());
    }

    @Inject(method = "renderLabelIfPresent", at = @At("TAIL"))
    private void unset(Entity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        NametagHook.INSTANCE.setShouldCancelBackground(false);
    }
}
