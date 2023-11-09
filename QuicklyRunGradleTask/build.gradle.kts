//参考: https://juejin.cn/post/7162815124539965453
//https://zhuanlan.zhihu.com/p/57541660
plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.8.20"
    id("org.jetbrains.intellij") version "1.13.3"//要为 IDEA 插件开发流程提供各方面的支持，例如运行、测试、部署等
    id("org.jetbrains.compose") version "1.4.1"
}

group = "com.lt"
version = "1.0.1"

repositories {
    mavenCentral()
}

// 这里是对文件头 org.jetbrains.intellij Gradle 插件的二次配置，默认生成的以下三项，您可以理解为是配置了一个简易版本的 JetBrains IDE 环境，用来运行和调试我们正在开发的这个 IDEA 插件
// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.2.5")// IDEA 插件运行调试版本
    type.set("IC") // Target IDE Platform // 指定运行在哪个 JetBrains 的 IDE 类型上，这里 IC 是 IntelliJ IDEA Community Edition 的缩写，您也可以指定 JetBrains 的其他 IDE 产品例如 CLion, PyCharm...

    plugins.set(listOf(/* Plugin Dependencies */"gradle"))// 指定上面这个 IDE 环境需要自带哪些插件，默认不需要
}

sourceSets {
    main {
        java.srcDirs("src/main/kotlin")
    }
    test {
        java.srcDirs("src/test/kotlin")
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    // 声明 IDEA 插件的版本支持范围
    patchPluginXml {
        sinceBuild.set("222")// 213 代表 2021.3
        untilBuild.set("232.*")// 223.* 代表 2022.3 及任意子版本
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.google.code.gson:gson:2.8.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}