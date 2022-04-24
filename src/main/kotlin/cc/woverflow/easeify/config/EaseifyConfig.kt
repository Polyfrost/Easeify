package cc.woverflow.easeify.config

import cc.woverflow.easeify.Easeify
import gg.essential.universal.ChatColor
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
        name = "Delayed Arrow Rendering",
        description = "Only render projectiles 2 ticks after they are shot to stop them from obstructing your view.",
        category = "Render"
    )
    var delayedArrowRendering = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Persistent Crosshair Rendering",
        description = "Render the crosshair even in third-person.",
        category = "Render",
        subcategory = "Crosshair"
    )
    var persistentCrosshair = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Remove Crosshair Transparency",
        description = "Remove the crosshair transparency.",
        category = "Render",
        subcategory = "Crosshair"
    )
    var removeCrosshairTransparency = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Render Own Nametag",
        description = "Render your own nametag in third person.",
        category = "Render",
        subcategory = "Nametags"
    )
    var renderOwnNametag = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Remove Nametag Background",
        description = "Remove the background of nametags.",
        category = "Render",
        subcategory = "Nametags"
    )
    var removeNametagBackground = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Nametag Text Shadow",
        description = "Render a text shadow with the nametag text.",
        category = "Render",
        subcategory = "Nametags"
    )
    var nametagTextShadow = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Persistent Nametag Rendering",
        description = "Render nametags even with the F1 mode enabled.",
        category = "Render",
        subcategory = "Nametags"
    )
    var persistentNametags = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Disable Crosshair in GUIs",
        description = "Disable your crosshair when you are in a GUI.",
        category = "Render"
    )
    var disableCrosshairGui = false

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
        description = "Modify your FOV when having the speed effect.",
        category = "Render",
        subcategory = "FOV",
        minF = -5f,
        maxF = 5f
    )
    var speedFOV = 1f

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Slowness FOV",
        description = "Modify your FOV when having the slowness effect.",
        category = "Render",
        subcategory = "FOV",
        minF = -5f,
        maxF = 5f
    )
    var slownessFOV = 1f

    @Property(
        type = PropertyType.SWITCH,
        name = "Remove Screen Bobbing",
        description = "Remove the bobbing of the screen ${ChatColor.COLOR_CHAR}lonly${ChatColor.COLOR_CHAR}r instead of toggling the whole bobbing feature.",
        category = "Render",
        subcategory = "Bobbing"
    )
    var removeScreenBobbing = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Remove Map Bobbing",
        description = "Remove the bobbing of the hand if a map is being held.",
        category = "Render",
        subcategory = "Bobbing"
    )
    var removeMapBobbing = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Static Items",
        description = "Remove the bobbing of items dropping in the ground.",
        category = "Render",
        subcategory = "Items"
    )
    var staticItems = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Unstacked Items",
        description = "Always render one item per stack of item.",
        category = "Render",
        subcategory = "Items"
    )
    var unstackedItems = false

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Fire Overlay Height",
        description = "Change the height of the fire overlay.",
        category = "Render",
        subcategory = "Overlays",
        minF = -0.5f,
        maxF = 0f
    )
    var fireOverlayHeight = 0f

    @Property(
        type = PropertyType.SWITCH,
        name = "Remove Water Overlay",
        description = "Remove the water overlay when submerged in water.",
        category = "Render",
        subcategory = "Overlays"
    )
    var removeWaterOverlay = false

    @Property(
        type = PropertyType.SELECTOR,
        name = "Hotbar Scrolling Type",
        description = "Modify the way the hotbar scrolls.",
        category = "General",
        options = ["Normal", "Reverse", "Off"]
    )
    var hotbarType = 0

    init {
        initialize()
    }
}
