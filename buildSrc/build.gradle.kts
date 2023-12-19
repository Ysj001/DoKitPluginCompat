plugins {
    `kotlin-dsl`
}

private val reposDir = File(rootDir, "../repos")

repositories {
    maven { url = reposDir.toURI() }
    maven { setUrl("https://maven.aliyun.com/nexus/content/groups/public/") }
    maven { setUrl("https://jitpack.io") }
    google()
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(gradleKotlinDsl())
    implementation("com.android.tools.build:gradle-api:8.1.1")
    val properties = org.jetbrains.kotlin
        .konan.properties
        .loadProperties(File(rootDir, "../gradle.properties").absolutePath)
    val groupId = properties["lib.groupId"] as String
    val dokitVersion = properties["dokit.version"] as String
    val hasPlugin = groupId
        .replace(".", File.separator)
        .let { File(reposDir, it) }
        .takeIf { it.isDirectory }
        ?.list()
        ?.filter { it == "dokit-plugin-compat" }
        .isNullOrEmpty()
        .not()
    if (hasPlugin) {
        implementation("$groupId:dokit-plugin-compat:$dokitVersion")
    }
    implementation(properties["bcu.plugin"] as String)
}
