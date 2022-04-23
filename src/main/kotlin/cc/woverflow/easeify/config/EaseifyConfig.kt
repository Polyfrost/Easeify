package cc.woverflow.easeify.config

import cc.woverflow.easeify.Easeify
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import java.io.File

object EaseifyConfig : Vigilant(File(Easeify.modDir, "${Easeify.ID}.toml")) {
    @Property(
        type = PropertyType.SWITCH,
        name = "Disable Text Shadow",
        description = "Disable the shadow on text rendering. Can boost performance.",
        category = "General"
    )
    var disableTextShadow = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Render Own Nametag",
        description = "Render your own nametag in third person.",
        category = "General",
        subcategory = "Nametags"
    )
    var renderOwnNametag = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Remove Nametag Background",
        description = "Remove the background of nametags.",
        category = "General",
        subcategory = "Nametags"
    )
    var removeNametagBackground = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Nametag Text Shadow",
        description = "Render a text shadow with the nametag text.",
        category = "General",
        subcategory = "Nametags"
    )
    var nametagTextShadow = true

    init {
        initialize()
    }
}
