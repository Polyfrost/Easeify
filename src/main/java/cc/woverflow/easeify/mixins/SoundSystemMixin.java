package cc.woverflow.easeify.mixins;

import cc.woverflow.easeify.hooks.SoundHook;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.WeightedSoundSet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {
    @Shadow @Final private SoundManager loader;

    @ModifyExpressionValue(method = "reloadSounds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;get(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/sound/WeightedSoundSet;"))
    private WeightedSoundSet onGetSound(WeightedSoundSet value) {
        if (value != null) {
            SoundHook.INSTANCE.registerSound(value);
        }
        return value;
    }

    @ModifyReturnValue(method = "getAdjustedVolume", at = @At("RETURN"))
    private float onGetAdjustedVolume(float value, SoundInstance sound) {
        WeightedSoundSet soundSet = sound.getSoundSet(loader);
        return soundSet != null ? SoundHook.INSTANCE.getAdjustedVolume(soundSet) * value : value;
    }
}
