package cc.woverflow.easeify.config

import cc.woverflow.easeify.Easeify
import gg.essential.vigilance.Vigilant
import java.io.File

object EaseifySoundConfig : Vigilant(File(Easeify.modDir, "sounds.toml")) {
    init {
        initialize()
    }
}
