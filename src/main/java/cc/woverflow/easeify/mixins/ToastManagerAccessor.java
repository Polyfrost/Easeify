package cc.woverflow.easeify.mixins;

import net.minecraft.client.toast.ToastManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ToastManager.class)
public interface ToastManagerAccessor {
    @Accessor("visibleEntries")
    ToastManager.Entry<?>[] getVisibleEntries();
}
