package cc.woverflow.easeify

import net.fabricmc.api.ClientModInitializer

class Easeify : ClientModInitializer {
    override fun onInitializeClient() {
        println("Hello Fabric world!")
    }

    companion object {
        const val NAME = "@NAME@"
        const val ID = "@ID@"
        const val VER = "@VER@"
    }
}
