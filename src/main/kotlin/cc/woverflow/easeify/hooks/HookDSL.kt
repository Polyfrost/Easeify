package cc.woverflow.easeify.hooks

//#if MC<11900
fun <T : Any> T.getValue(): T {
    return this
}
//#endif
