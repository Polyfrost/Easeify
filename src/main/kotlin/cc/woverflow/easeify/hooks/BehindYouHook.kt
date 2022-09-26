package cc.woverflow.easeify.hooks

import cc.woverflow.easeify.config.EaseifyConfig
import gg.essential.universal.UKeyboard
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.option.Perspective
import net.minecraft.client.util.InputUtil

object BehindYouHook {
    private var previousPerspective = Perspective.FIRST_PERSON
    private var previousFOV = 0.0

    private val backKeybind = KeyBinding("BehindYou (Back)", InputUtil.Type.KEYSYM, UKeyboard.KEY_NONE, "BehindYouV3")
    private var previousBackKey = false
    private var backToggled = false

    private val frontKeybind = KeyBinding("BehindYou (Front)", InputUtil.Type.KEYSYM, UKeyboard.KEY_NONE, "BehindYouV3")
    private var previousFrontKey = false
    private var frontToggled = false

    fun initialize() {
        KeyBindingHelper.registerKeyBinding(frontKeybind)
        KeyBindingHelper.registerKeyBinding(backKeybind)
    }

    fun onTick() {
        if (MinecraftClient.getInstance().currentScreen != null || MinecraftClient.getInstance().world == null || MinecraftClient.getInstance().player == null) {
            if (EaseifyConfig.frontKeybindMode == 0 || EaseifyConfig.backKeybindMode == 0) {
                resetAll()
            }
            return
        }

        val backDown = backKeybind.isPressed
        val frontDown = frontKeybind.isPressed

        if (backDown && frontDown) return

        if (backDown != previousBackKey) {
            previousBackKey = backDown

            if (backDown) {
                if (backToggled) {
                    resetBack()
                } else {
                    if (frontToggled) {
                        resetFront()
                    }
                    enterBack()
                }
            } else if (EaseifyConfig.backKeybindMode == 0) {
                resetBack()
            }
            setPerspective()

        } else if (frontDown != previousFrontKey) {
            previousFrontKey = frontDown

            if (frontDown) {
                if (frontToggled) {
                    resetFront()
                } else {
                    if (backToggled) {
                        resetBack()
                    }
                    enterFront()
                }
            } else if (EaseifyConfig.frontKeybindMode == 0) {
                resetFront()
            }

            setPerspective()
        }
    }

    private fun setPerspective() {
        MinecraftClient.getInstance().gameRenderer.onCameraEntitySet(if (MinecraftClient.getInstance().options.perspective.isFirstPerson) MinecraftClient.getInstance().getCameraEntity() else null)
    }

    private fun enterBack() {
        backToggled = true
        previousPerspective = getPerspective()
        previousFOV = getFOV()
        setPerspective(2)
        if (EaseifyConfig.changeFOV) {
            setFOV(EaseifyConfig.backFOV)
        }
    }

    private fun enterFront() {
        frontToggled = true
        previousPerspective = getPerspective()
        previousFOV = getFOV()
        setPerspective(1)
        if (EaseifyConfig.changeFOV) {
            setFOV(EaseifyConfig.frontFOV)
        }
    }

    private fun resetBack() {
        backToggled = false
        setPerspective(
            previousPerspective.ordinal
        )
        setFOV(previousFOV)
    }

    private fun resetFront() {
        frontToggled = false
        setPerspective(previousPerspective.ordinal)
        setFOV(previousFOV)
    }

    private fun resetAll() {
        if (frontToggled) {
            resetFront()
        }
        if (backToggled) {
            resetBack()
        }
    }

    private fun getPerspective() = MinecraftClient.getInstance().options.perspective

    private fun setPerspective(value: Int) {
        MinecraftClient.getInstance().options.perspective = Perspective.values()[value]
    }

    private fun getFOV() = MinecraftClient.getInstance().options.fov.getValue().toDouble()

    private fun setFOV(value: Number) {
        //#if MC<11900
        MinecraftClient.getInstance().options.fov = value.toDouble()
        //#else
        //$$ MinecraftClient.getInstance().options.fov.setValue(value.toInt())
        //#endif
    }
}
