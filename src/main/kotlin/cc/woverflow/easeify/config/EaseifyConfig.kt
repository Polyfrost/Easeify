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
        category = "Render"
    )
    var disableTextShadow = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Render Own Nametag",
        description = "Render your own nametag in third person.",
        category = "Render"
    )
    var renderOwnNametag = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Remove Nametag Background",
        description = "Remove the background of nametags.",
        category = "Render"
    )
    var removeNametagBackground = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Nametag Text Shadow",
        description = "Render a text shadow with the nametag text.",
        category = "Render"
    )
    var nametagTextShadow = true

    @Property(
        type = PropertyType.SWITCH,
        name = "FOV Modifier",
        description = "Allow for modifying FOV change states.",
        category = "Render",
        subcategory = "FOV"
    )
    var fovModifying = false

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Sprinting FOV",
        description = "Modify your FOV when sprinting.",
        category = "Render",
        subcategory = "FOV",
        minF = -5f,
        maxF = 5f
    )
    var sprintingFOV = 1f

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Flying FOV",
        description = "Modify your FOV when flying.",
        category = "Render",
        subcategory = "FOV",
        minF = -5f,
        maxF = 5f
    )
    var flyingFOV = 1f

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Bow FOV",
        description = "Modify your FOV when pulling back a bow.",
        category = "Render",
        subcategory = "FOV",
        minF = -5f,
        maxF = 5f
    )
    var bowFOV = 1f

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Spyglass FOV",
        description = "Modify your FOV when using a Spyglass.",
        category = "Render",
        subcategory = "FOV",
        minF = -5f,
        maxF = 5f
    )
    var spyglassFOV = 1f

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Speed FOV",
        description = "Modify your FOV when having the speed effect.\n" +
                "This will also work with any other effect that makes you faster.",
        category = "Render",
        subcategory = "FOV",
        minF = -5f,
        maxF = 5f
    )
    var speedFOV = 1f

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Slowness FOV",
        description = "Modify your FOV when having the slowness effect.\nThis will also work with any other effect that slows you down.",
        category = "Render",
        subcategory = "FOV",
        minF = -5f,
        maxF = 5f
    )
    var slownessFOV = 1f

    init {
        initialize()
    }
}
