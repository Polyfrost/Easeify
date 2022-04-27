package cc.woverflow.easeify.hooks

import cc.woverflow.easeify.config.ConfigUtil.createAndRegisterConfig
import gg.essential.vigilance.data.PropertyData
import gg.essential.vigilance.data.PropertyType
import net.minecraft.client.sound.WeightedSoundSet
import net.minecraft.text.TranslatableText
import java.util.*


object SoundHook {
    private val propertyMap = hashMapOf<WeightedSoundSet, PropertyData>()

    fun registerSound(value: WeightedSoundSet) {
        val text = value.subtitle
        if (text != null) {
            val translated: String? = if (text is TranslatableText) text.key else null
            propertyMap[value] = createAndRegisterConfig(
                PropertyType.SLIDER,
                value.sound.location.path.substringAfter("/").substringBefore("/").capitalize(),
                value.sound.location.path.substringAfter("/").let { it.substringAfter(it.substringBefore("/")) }.substringAfter("/").let { if (it.contains("/")) it.substringBefore("/") else it }.capitalize(),
                (translated ?: text.asString()).replace(".", "_"),
                translated ?: text.asString(),
                "",
                100,
                0,
                200
            ) { }
        }
    }

    fun getAdjustedVolume(sound: WeightedSoundSet): Float {
        propertyMap[sound]?.let {
            val asAny = it.getAsAny()
            if (asAny is Int) return asAny.toFloat() / 100f
        }
        return 1f
    }

    private fun String.capitalize() = this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() }
}
