package cc.woverflow.easeify.utils

import cc.woverflow.easeify.Easeify
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import gg.essential.universal.UChat
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicHeader
import org.apache.http.util.EntityUtils
import java.io.File
import java.io.FileOutputStream

/**
 * Stolen from Skytils under AGPLv3
 * https://github.com/Skytils/SkytilsMod/blob/1.x/LICENSE.md
 */
object APIUtil {
    private val builder: HttpClientBuilder =
        HttpClients.custom().setUserAgent("Easeify/${Easeify.VER}")
            .setConnectionManagerShared(true)
            .setDefaultHeaders(
                 mutableListOf(
                    BasicHeader("Pragma", "no-cache"),
                    BasicHeader("Cache-Control", "no-cache")
                )
            )
            .setDefaultRequestConfig(
                RequestConfig.custom()
                    .setConnectTimeout(30000)
                    .setConnectionRequestTimeout(30000)
                    .build()
            )
            .useSystemProperties()

    fun getString(url: String): String? {
        val client = builder.build()
        try {
            client.execute(HttpGet(url)).use { response ->
                if (response.statusLine.statusCode == 200) {
                    return EntityUtils.toString(response.entity)
                }
            }
        } catch (ex: Throwable) {
            UChat.chat("Easeify ran into an ${ex::class.simpleName ?: "error"} whilst fetching a resource. See logs for more details.")
            ex.printStackTrace()
        } finally {
            client.close()
        }
        return null
    }

    fun download(url: String, file: File): Boolean {
        val escapedUrl = url.replace(" ", "%20")
        try {
            FileOutputStream(file).use { fileOut ->
                builder.build().execute(HttpGet(escapedUrl)).use { response ->
                    val buffer = ByteArray(1024)
                    var read: Int
                    while (response.entity.content.read(buffer).also { read = it } > 0) {
                        fileOut.write(buffer, 0, read)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }
}

fun APIUtil.getJsonElement(url: String): JsonElement? = try { JsonParser.parseString(getString(url)) } catch (e: Exception) { e.printStackTrace(); null }
