package cc.woverflow.easeify.mixins;

import cc.woverflow.easeify.config.EaseifyConfig;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Mouse.class)
public class MouseMixin {
    @ModifyArg(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V"), index = 0)
    private double modifyScroll(double scrollAmount) {
        return switch (EaseifyConfig.INSTANCE.getHotbarType()) {
            case 0 -> scrollAmount;
            case 1 -> -scrollAmount;
            case 2 -> 0;
            default -> throw new IllegalStateException("Unexpected value: " + EaseifyConfig.INSTANCE.getHotbarType());
        };
    }
}
