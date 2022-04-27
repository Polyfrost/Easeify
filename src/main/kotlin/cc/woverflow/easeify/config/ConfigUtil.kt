package cc.woverflow.easeify.config

import gg.essential.vigilance.data.PropertyAttributesExt
import gg.essential.vigilance.data.PropertyData
import gg.essential.vigilance.data.PropertyType
import gg.essential.vigilance.data.ValueBackedPropertyValue
import java.util.function.Consumer

/**
 * Taken from Patcher under CC-NC-SA 4.0
 * https://github.com/Sk1erLLC/Patcher/blob/master/LICENSE.md
 */
object ConfigUtil {
    @JvmStatic
    fun createAndRegisterConfig(
        type: PropertyType, category: String, subCategory: String, name: String,
        translatedName: String, description: String, defaultValue: Any?, min: Int, max: Int, onUpdate: Consumer<Any?>
    ): PropertyData {
        val config = createConfig(type, category, subCategory, name, translatedName, description, defaultValue, min, max, onUpdate)
        register(config)
        return config
    }

    private fun createConfig(
        type: PropertyType, category: String, subCategory: String, name: String,
        translatedName: String, description: String, defaultValue: Any?, min: Int, max: Int, onUpdate: Consumer<Any?>
    ): PropertyData {
        val attributes = PropertyAttributesExt(type, name, category, subCategory, description, min, max, i18nName = translatedName)
        val data = PropertyData(attributes, ValueBackedPropertyValue(defaultValue), EaseifySoundConfig)
        data.setCallbackConsumer(onUpdate)
        return data
    }

    private fun register(data: PropertyData?) {
        EaseifySoundConfig.registerProperty(data!!)
    }
}
