package com.newestaf

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.kotlin.dsl.create

class BukkitExecutorPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create<BukkitExecutorExtension>("bukkitExecutor")
        target.extensions.add("barcodeBukkitExecutor", extension)

        target.tasks.register("runBukkit", BukkitExecutorTask::class.java).configure {
            this.isEnabled = extension.enabled.getOrElse(true)
            this.bukkitDir.set(extension.bukkitDir)
            this.archiveTask.set(extension.archiveTask)
            this.bukkitFileName.set(extension.bukkitFileName)
        }
    }
}
