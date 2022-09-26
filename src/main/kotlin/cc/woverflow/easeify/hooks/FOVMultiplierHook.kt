package cc.woverflow.easeify.hooks

import cc.woverflow.easeify.config.EaseifyConfig
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Items
import net.minecraft.util.math.MathHelper

/**
 * Adapted from Hyperium under LGPL 3.0
 * https://github.com/HyperiumClient/Hyperium/blob/master/LICENSE
 */
object FOVMultiplierHook {
    private val BOW_FOVS = mapOf(
        1 to 3.7497282E-4f,
        2 to 0.0015000105f,
        3 to 0.0033749938f,
        4 to 0.0059999824f,
        5 to 0.009374976f,
        6 to 0.013499975f,
        7 to 0.01837498f,
        8 to 0.023999989f,
        9 to 0.030375004f,
        10 to 0.037500024f,
        11 to 0.04537499f,
        12 to 0.05400002f,
        13 to 0.063374996f,
        14 to 0.07349998f,
        15 to 0.084375024f,
        16 to 0.096000016f,
        17 to 0.10837501f,
        18 to 0.121500015f,
        19 to 0.13537502f,
        20 to 0.14999998f
    )

    private const val FLYING_MODIFIER = 0.1f
    private const val SPRINTING_MODIFIER = 0.1500001f
    private const val SPEED_MODIFIER = 0.1f
    private const val SLOWNESS_MODIFIER = -0.075f
    private const val SPYGLASS_MODIFIER = 0.9f

    fun modifyFOVMultiplier(multiplier: Float): Float {
        return if (EaseifyConfig.fovModifying) {
            var initial = 1.0f
            MinecraftClient.getInstance().cameraEntity?.let { uncasted ->
                (uncasted as AbstractClientPlayerEntity).run {
                    if (abilities.flying) {
                        initial += FLYING_MODIFIER * EaseifyConfig.flyingFOV
                    }
                    if (isSprinting) {
                        initial += SPRINTING_MODIFIER * EaseifyConfig.sprintingFOV
                    }
                    for (effect in activeStatusEffects) {
                        if (effect.key == StatusEffects.SLOWNESS) {
                            initial += SLOWNESS_MODIFIER * (effect.value.amplifier + 1) * EaseifyConfig.slownessFOV
                        } else if (effect.key == StatusEffects.SPEED) {
                            initial += SPEED_MODIFIER * (effect.value.amplifier + 1) * EaseifyConfig.speedFOV
                        }
                    }
                    if (initial.isNaN() || initial.isInfinite()) {
                        initial = 1.0f
                    }
                    if (this.isUsingItem) {
                        if (activeItem.isOf(Items.BOW)) {
                            val duration = itemUseTime.coerceAtMost(20)
                            BOW_FOVS[duration]?.let {
                                initial -= it * EaseifyConfig.bowFOV
                            }
                        } else if (MinecraftClient.getInstance().options.perspective.isFirstPerson && this.isUsingSpyglass) {
                            initial = 1f - SPYGLASS_MODIFIER * EaseifyConfig.spyglassFOV
                            return@run
                        }
                    }
                    initial = MathHelper.lerp(MinecraftClient.getInstance().options.fovEffectScale.getValue().toFloat(), 1.0f, initial)
                    return@run
                }
            }
            initial
        } else {
            multiplier
        }
    }
}
