plugins {
    id("java-library")
    id("kotlin")
}

group = properties["lib.groupId"] as String
version = properties["dokit.version"] as String

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(gradleApi())
    implementation(properties["bcu.plugin.api"] as String)
    compileOnly("com.android.tools.build:gradle-api:$ANDROID_GRADLE_VERSION")
    implementation(dokit_plugin)
}

mavenPublish()