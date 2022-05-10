package cc.woverflow.easeify

import cc.woverflow.easeify.hooks.BehindYouHook
import cc.woverflow.easeify.utils.APIUtil
import cc.woverflow.easeify.utils.getJsonElement
import cc.woverflow.easeify.utils.showClickableToast
import gg.essential.universal.UDesktop
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import java.io.File
import java.net.URI

class Easeify : ClientModInitializer {
    override fun onInitializeClient() {
        if (!modDir.exists()) {
            modDir.mkdirs()
        }
        BehindYouHook.initialize()
        ClientTickEvents.START_WORLD_TICK.register {
            if (!hasChecked) {
                hasChecked = true
                // do checking
                CoroutineScope(Dispatchers.IO + CoroutineName("Easeify Update Checker")).launch {
                    try {
                        APIUtil.getJsonElement("https://api.github.com/repos/W-OVERFLOW/$ID/releases/latest")?.asJsonObject?.let { latestRelease ->
                            UpdateVersion(
                                latestRelease["tag_name"].asString.substringAfter("v"),
                                latestRelease["html_url"].asString
                            ).let {
                                if (UpdateVersion(VER) < it) {
                                    showClickableToast("Easeify is out of date!",
                                        "Please update to version ${it.version} by clicking here.", duration = 10000L) {
                                        it.url?.let { it1 -> URI.create(it1) }?.let { it2 -> UDesktop.browse(it2) }
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    companion object {
        const val NAME = "@NAME@"
        const val ID = "@ID@"
        const val VER = "@VER@"
        val modDir = File("./W-OVERFLOW/$NAME")
        private var hasChecked = false
        val updateRegex = Regex("^(?<version>[\\d.]+)-?(?<type>\\D+)?(?<typever>\\d+\\.?\\d*)?$")

        /**
         * Adapted from SimpleTimeChanger under AGPLv3
         * https://github.com/My-Name-Is-Jeff/SimpleTimeChanger/blob/master/LICENSE
         */
        private class UpdateVersion(val version: String, val url: String? = null) : Comparable<UpdateVersion> {

            private val matched = updateRegex.find(version)

            val isSafe = matched != null

            val versionArtifact = Version.fromString(matched!!.groups["version"]!!.value) ?: throw NullPointerException()
            val specialVersionType = run {
                val typeString = matched!!.groups["type"]?.value ?: return@run UpdateType.RELEASE

                return@run UpdateType.values().find { typeString == it.prefix } ?: UpdateType.UNKNOWN
            }
            val specialVersion = run {
                if (specialVersionType == UpdateType.RELEASE) return@run null
                return@run matched!!.groups["typever"]?.value?.toDoubleOrNull()
            }

            override fun compareTo(other: UpdateVersion): Int {
                if (!isSafe || !other.isSafe) return -1
                return if (versionArtifact.compareTo(other.versionArtifact) == 0) {
                    if (specialVersionType.ordinal == other.specialVersionType.ordinal) {
                        (specialVersion ?: 0.0).compareTo(other.specialVersion ?: 0.0)
                    } else other.specialVersionType.ordinal - specialVersionType.ordinal
                } else versionArtifact.compareTo(other.versionArtifact)
            }
        }

        class Version(
            private val major: Int, private val minor: Int = 0, private val patch: Int = 0
        ) {
            operator fun compareTo(other: Version): Int {
                var result = major - other.major
                if (result == 0) {
                    result = minor - other.minor
                    if (result == 0) {
                        result = patch - other.patch
                    }
                }

                return result
            }

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other !is Version) return false

                return compareTo(other) == 0
            }

            override fun toString(): String = "$major.$minor.$patch"

            override fun hashCode(): Int {
                var result = major
                result = 31 * result + minor
                result = 31 * result + patch
                return result
            }

            companion object {
                val regex = Regex("(?<major>\\d+)(?:\\.(?<minor>\\d+)(?:\\.(?<patch>\\d+)?)?)?")

                fun fromString(version: String): Version? {
                    val match = regex.find(version)
                    return if (match != null) {
                        Version(
                            match.groups["major"]!!.value.toInt(),
                            match.groups["minor"]?.value?.toInt() ?: 0,
                            match.groups["patch"]?.value?.toInt() ?: 0
                        )
                    } else {
                        null
                    }
                }
            }
        }

        enum class UpdateType(val prefix: String) {
            RELEASE(""), PRERELEASE("pre"), BETA("beta"), ALPHA("alpha"), UNKNOWN("unknown")
        }
    }
}
