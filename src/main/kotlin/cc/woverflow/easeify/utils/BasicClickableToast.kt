package cc.woverflow.easeify.utils

import cc.woverflow.easeify.mixins.ToastManagerAccessor
import dev.cbyrne.toasts.handler.ToastDecayHandler
import dev.cbyrne.toasts.impl.BasicToast
import gg.essential.universal.UMouse
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.toast.Toast
import net.minecraft.client.toast.ToastManager
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW

class BasicClickableToast(title: Text, description: Text, displayTime: Long, private val decayHandler: ToastDecayHandler?, icon: Identifier, private val onClick: () -> Unit) : BasicToast(title, description, displayTime, decayHandler, icon) {
    private var hasPressed = false
    private var shouldCheck = false

    override fun draw(matrices: MatrixStack?, manager: ToastManager?, startTime: Long): Toast.Visibility {
        val value = super.draw(matrices, manager, startTime)
        if (hasPressed) {
            return Toast.Visibility.HIDE
        } else {
            shouldCheck = value == Toast.Visibility.SHOW
        }
        return value
    }

    private fun getY(manager: ToastManager?): Int {
        manager?.let {
            val toasts = (it as ToastManagerAccessor).visibleEntries
            if (toasts.isEmpty()) {
                return@let
            }
            toasts.forEachIndexed { i, toast ->
                if (toast.instance == this) {
                    return i * height
                }
            }
        }
        return 0
    }

    override fun show() {
        super.show()
        ClientTickEvents.START_CLIENT_TICK.register {
            if (shouldCheck) {
                shouldCheck = false
                if (UMouse.Scaled.x >= MinecraftClient.getInstance().window.scaledWidth - width && UMouse.Scaled.x <= MinecraftClient.getInstance().window.scaledWidth && UMouse.Scaled.y >= 0 && UMouse.Scaled.y <= getY(MinecraftClient.getInstance().toastManager) + height) {
                    if (GLFW.glfwGetMouseButton(MinecraftClient.getInstance().window.handle, 0) == GLFW.GLFW_PRESS) {
                        hasPressed = true
                        onClick()
                        decayHandler?.onDecay()
                    }
                }
            }
        }
    }
}
