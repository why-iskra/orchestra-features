object ProjectInfo {

    const val GROUP_ID = "ru.unit.orchestra-features"
    const val VERSION = "1.0.2"

    sealed class ArtifactId(val name: String) {

        object Common : ArtifactId("common")

        object InteractiveAndroid : ArtifactId("interactive-android")

        object Processor : ArtifactId("Processor")
    }
}