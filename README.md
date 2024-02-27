## DoKitPluginCompat

由于 [DoKit](https://github.com/didi/DoKit) 插件在官方并不兼容 `AGP8+`，因此如果你想在高版本的 `AGP` 上使用 `DoKit` 可以使用本项目的兼容插件包。兼容插件包中对字节码的修改基于 [BCU](https://github.com/Ysj001/BytecodeUtil) 的 `IModifier` 接口，通过该接口的实现类来代理并驱动 `DoKit` 插件中的所有 `Transformer` 进而兼容原 `Dokit` 插件的功能。

本兼容包的主版本和 DoKit 保持一致便于理解和使用，例子：

- 官方插件依赖：io.github.didi.dokit:dokitx-plugin:3.7.1
- 兼容插件依赖：com.github.Ysj001:DoKitPluginCompat:3.7.1-v1

**注意！不要同时使用兼容插件和官方插件**



### Compile

如果你要编译本项目，可了解如下工程结构

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
           classpath("com.github.Ysj001:DoKitPluginCompat:<lastest-version>")
       }
   }
   
   subprojects {
       repositories {
           maven { url 'https://jitpack.io' }
       }
   }
   ```

3. 在 `app` 模块的 `build.gradle.kts` 中的配置如下

   - [kts 脚本写法例子点我](app/build.gradle.kts)
   - [groovy 脚本写法例子点我](app/groovy_demo_build.gradle)

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



### Other

- 如果本项目对你有所帮助，欢迎 start。
- 如果有问题或 bug 欢迎提 issues 给我。
