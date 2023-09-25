open class Dependency(
    open val group: String,
    open val module: String,
    open val version: Version
) {

    fun resolve() = "$group:$module:${version.version}"
}