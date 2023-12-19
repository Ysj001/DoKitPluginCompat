rootProject.name = "DoKitPluginCompat"

include(":dokit_plugin_compat")

private val hasPlugin = File(rootDir, "gradle.properties")
    .inputStream()
    .use { java.util.Properties().apply { load(it) } }
    .getProperty("lib.groupId")
    .replace(".", File.separator)
    .let { File(File(rootDir, "repos"), it) }
    .takeIf { it.isDirectory }
    ?.list()
    ?.filter { it == "dokit-plugin-compat" }
    .isNullOrEmpty()
    .not()

if (hasPlugin) {
    // Demo
    include(":app")
}
