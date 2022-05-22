package cc.woverflow.easeify.mixins;

import cc.woverflow.easeify.config.EaseifyConfig;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    public void Easeify$modifyRenderType(BlockState state, CallbackInfoReturnable<BlockRenderType> cir) {
        if (EaseifyConfig.INSTANCE.getRemoveGroundFoliage()) {
            //No, i wont figure out how to use block tags in this
            if (state.isOf(Blocks.GRASS) || state.isOf(Blocks.TALL_GRASS) || state.isOf(Blocks.DANDELION) || state.isOf(Blocks.POPPY) || state.isOf(Blocks.BLUE_ORCHID) || state.isOf(Blocks.ALLIUM) || state.isOf(Blocks.AZURE_BLUET) || state.isOf(Blocks.ORANGE_TULIP) || state.isOf(Blocks.PINK_TULIP) || state.isOf(Blocks.RED_TULIP) || state.isOf(Blocks.WHITE_TULIP) || state.isOf(Blocks.OXEYE_DAISY) || state.isOf(Blocks.CORNFLOWER) || state.isOf(Blocks.LILY_OF_THE_VALLEY) || state.isOf(Blocks.SUNFLOWER) || state.isOf(Blocks.LILAC) || state.isOf(Blocks.ROSE_BUSH) || state.isOf(Blocks.PEONY) || state.isOf(Blocks.BROWN_MUSHROOM) || state.isOf(Blocks.RED_MUSHROOM) || state.isOf(Blocks.FERN) || state.isOf(Blocks.LARGE_FERN) || state.isOf(Blocks.SEAGRASS) || state.isOf(Blocks.TALL_SEAGRASS)) {
                cir.setReturnValue(BlockRenderType.INVISIBLE);
            }
        }
    }
}
