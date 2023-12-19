## DoKitPluginCompat

[DoKit](https://github.com/didi/DoKit) 插件兼容高版本 AGP（8+） 的实现。



### Compile

工程结构如下

- `app` 用于演示
- `buildSrc` 管理 maven 发布和版本控制项目统一配置
- `repos` 本地 maven 仓库，便于开发时调试
- `dokit_plugin_compat` 本项目核心实现

在编译本项目前请先在根目录下执行 `gradlew publishAllPublicationsToLocalRepository` 然后重新 sync。



### Quick Start

1. 推荐使用下面所示的最新版本：

   - BCU：[![](https://jitpack.io/v/Ysj001/BytecodeUtil.svg)](https://jitpack.io/#Ysj001/BytecodeUtil)
   - DoKitPluginCompat：[![](https://jitpack.io/v/Ysj001/DoKitPluginCompat.svg)](https://jitpack.io/#Ysj001/DoKitPluginCompat)

2. 在项目的根 `build.gradle.kts` 中配置如下

   ```kotlin
   // Top-level build file
   buildscript {
       repositories {
           maven { setUrl("https://jitpack.io") }
       }
       
       dependencies {
           // BCU 插件依赖
           classpath("com.github.Ysj001.BytecodeUtil:plugin:<lastest-version>")
           // DoKitPluginCompat 依赖
           classpath("com.github.Ysj001.DoKitPluginCompat:dokit-plugin-compat:<lastest-version>")
       }
   }
   
   subprojects {
       repositories {
           maven { url 'https://jitpack.io' }
       }
   }
   ```

3. 在 `app` 模块的 `build.gradle.kts` 中的配置如下

   ```kotlin
   plugins {
       id("com.android.application")
       id("org.jetbrains.kotlin.android")
       // 添加 bcu 插件
       id("bcu-plugin")
       // 添加 dokit 兼容插件
       id("dokit-compat")
   }
   
   // 配置 bcu 插件
   bcu {
       config { variant ->
           loggerLevel = 2
           modifiers = arrayOf(
           	// 添加 DoKit 插装兼容实现
           	Class.forName("com.ysj.lib.dokit.plugin.compat.DoKitTransformCompat"),
           )
       }
       filterNot { variant, entryName ->
           // 请按需配置过滤，可大幅提升编译输速度
           false
       }
   }
   
   // 配置 dokit
   // 详见：https://xingyun.xiaojukeji.com/docs/dokit/#/androidGuide
   dokitExt {
       comm {
           // ...
       }
       slowMethod {
           // ...
       }
   }
   
   ```
   
4. 可参考 `app` 模块中的样例配置。



### Other

- 如果本项目对你有所帮助，欢迎 start。
- 如果有问题或 bug 欢迎提 issues 给我。
