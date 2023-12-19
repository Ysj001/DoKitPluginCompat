package com.ysj.lib.dokit.plugin.compat

import com.android.build.api.variant.ApplicationVariant
import com.android.build.api.variant.Variant
import com.didichuxing.doraemonkit.plugin.DoKitExtUtil
import com.didichuxing.doraemonkit.plugin.classtransformer.BigImgClassTransformer
import com.didichuxing.doraemonkit.plugin.classtransformer.CommClassTransformer
import com.didichuxing.doraemonkit.plugin.classtransformer.EnterMSClassTransformer
import com.didichuxing.doraemonkit.plugin.classtransformer.GSMClassTransformer
import com.didichuxing.doraemonkit.plugin.extension.SlowMethodExt
import com.didiglobal.booster.transform.ArtifactManager
import com.didiglobal.booster.transform.Klass
import com.didiglobal.booster.transform.KlassPool
import com.didiglobal.booster.transform.TransformContext
import com.ysj.lib.bytecodeutil.plugin.api.IModifier
import org.gradle.api.Project
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.util.concurrent.Executor

/**
 * DoKit 插装兼容。
 *
 * @author Ysj
 * Create time: 2023/12/18
 */
class DoKitTransformCompat(
    override val executor: Executor,
    override val allClassNode: Map<String, ClassNode>,
) : IModifier {

    private val transforms = listOf(
        CommClassTransformer(),
        BigImgClassTransformer(),
        GSMClassTransformer(),
        EnterMSClassTransformer(),
    )
    private lateinit var context: TransformContext

    override fun initialize(project: Project, variant: Variant) {
        super.initialize(project, variant)
        DoKitExtUtil.DOKIT_PLUGIN_SWITCH = project.properties["DOKIT_PLUGIN_SWITCH"]
            ?.toString()?.toBooleanStrictOrNull() ?: true
        DoKitExtUtil.DOKIT_LOG_SWITCH = project.properties["DOKIT_LOG_SWITCH"]
            ?.toString()?.toBooleanStrictOrNull() ?: false
        DoKitExtUtil.SLOW_METHOD_SWITCH = project.properties["DOKIT_METHOD_SWITCH"]
            ?.toString()?.toBooleanStrictOrNull() ?: false
        DoKitExtUtil.SLOW_METHOD_STRATEGY = project.properties["DOKIT_METHOD_STRATEGY"]
            ?.toString()?.toIntOrNull() ?: SlowMethodExt.STRATEGY_STACK
        DoKitExtUtil.STACK_METHOD_LEVEL = project.properties["DOKIT_METHOD_STACK_LEVEL"]
            ?.toString()?.toIntOrNull() ?: 5
        DoKitExtUtil.WEBVIEW_CLASS_NAME = project.properties["DOKIT_WEBVIEW_CLASS_NAME"]
            ?.toString() ?: ""
        val applicationId = if (variant is ApplicationVariant) variant.applicationId.get() else ""
        context = MTransformContext(
            applicationId = applicationId,
            buildDir = project.buildDir,
            isDataBindingEnabled = false,
            isDebuggable = true,
            originalApplicationId = applicationId,
            projectDir = project.projectDir,
            reportsDir = File(project.buildDir, "reports").also { it.mkdirs() },
            temporaryDir = File(project.buildDir, "temp"),
        )
    }

    override fun scan(classNode: ClassNode) {
    }

    override fun modify() {
        allClassNode.forEach { entry ->
            transforms.forEach {
                it.transform(context, entry.value)
            }
        }
    }

    private inner class MTransformContext(
        override val applicationId: String,
        override val artifacts: ArtifactManager = object : ArtifactManager {},
        override val bootClasspath: Collection<File> = emptyList(),
        override val buildDir: File,
        override val compileClasspath: Collection<File> = emptyList(),
        override val dependencies: Collection<String> = emptyList(),
        override val isDataBindingEnabled: Boolean,
        override val isDebuggable: Boolean,
        override val klassPool: KlassPool = object : KlassPool {
            override val classLoader: ClassLoader = javaClass.classLoader
            override val parent: KlassPool? = null
            override fun close() = Unit
            override fun get(type: String): Klass {
                throw RuntimeException("not support")
            }
        },
        override val name: String = "BcuDoKitCompat",
        override val originalApplicationId: String,
        override val projectDir: File,
        override val reportsDir: File,
        override val runtimeClasspath: Collection<File> = emptyList(),
        override val temporaryDir: File
    ) : TransformContext {
        override fun hasProperty(name: String): Boolean = false
    }

}