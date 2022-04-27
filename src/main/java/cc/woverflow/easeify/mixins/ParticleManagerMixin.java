package cc.woverflow.easeify.mixins;

import cc.woverflow.easeify.config.EaseifyConfig;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    private void onBlockBreakParticle(Particle particle, CallbackInfo ci) {
        if (EaseifyConfig.INSTANCE.getDisableBlockParticles() && particle instanceof BlockDustParticle) {
            ci.cancel();
        }
    }
}
