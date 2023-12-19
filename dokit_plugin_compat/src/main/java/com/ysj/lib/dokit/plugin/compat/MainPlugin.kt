package com.ysj.lib.dokit.plugin.compat

import com.android.build.api.variant.AndroidComponentsExtension
import com.didichuxing.doraemonkit.plugin.DoKitExtUtil
import com.didichuxing.doraemonkit.plugin.extension.DoKitExt
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 插件入口
 *
 * @author Ysj
 * Create time: 2023/12/19
 */
class MainPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val androidExt = target
            .extensions
            .findByType(AndroidComponentsExtension::class.java)
            ?: return
        target.extensions.create(
            "dokitExt",
            DoKitExt::class.java,
        )
        androidExt.onVariants {
            DoKitExtUtil.init(
                target
                    .extensions
                    .findByType(DoKitExt::class.java)
                    ?: return@onVariants
            )
        }
    }

}