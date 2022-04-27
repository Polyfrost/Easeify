package cc.woverflow.easeify.utils

import dev.cbyrne.toasts.impl.builder.BasicToastBuilder
import net.minecraft.util.Identifier

fun toast(builder: BasicToastBuilder.() -> Unit) {
    BasicToastBuilder().apply(builder).build().show()
}

fun showToast(title: String, description: String, icon: Identifier = Identifier("easeify", "icon.png"), duration: Long = 5000L, onDecay: () -> Unit = {}) = toast {
    title(title)
    description(description)
    icon(icon)
    displayTime(duration)
    decayHandler(onDecay)
}
