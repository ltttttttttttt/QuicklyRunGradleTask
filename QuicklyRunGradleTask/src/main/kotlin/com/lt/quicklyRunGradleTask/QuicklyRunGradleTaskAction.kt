package com.lt.quicklyRunGradleTask

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.DumbAwareAction
import com.lt.quicklyRunGradleTask.config.ConfigUtil
import com.lt.quicklyRunGradleTask.util.LoadingDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.gradle.tooling.GradleConnectionException
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ResultHandler
import org.jetbrains.skiko.MainUIDispatcher
import java.io.File

class QuicklyRunGradleTaskAction : DumbAwareAction() {

    override fun actionPerformed(e: AnActionEvent) {
        //获取或初始化配置
        val config = ConfigUtil.getConfig(e)
        val cmdString = config?.cmdString
        if (cmdString.isNullOrEmpty()) {
            ConfigAction().actionPerformed(e)
            return
        }

        //运行命令,比如: ":desktop:run"
        val project = e.getRequiredData(CommonDataKeys.PROJECT)
        GlobalScope.launch {
            var loadingDialog: LoadingDialog? = null
            GlobalScope.launch(MainUIDispatcher) {
                if (config.runShowLoading)
                    loadingDialog = LoadingDialog(e, "$cmdString Running")
                loadingDialog?.show()
            }
            val gradleConnector = GradleConnector.newConnector()
            val projectConnection = gradleConnector.forProjectDirectory(File(project.basePath)).connect()
            projectConnection.newBuild().forTasks(cmdString).run(object : ResultHandler<Void?> {
                override fun onComplete(p0: Void?) {
                    println("QuicklyRunGradleTask onComplete")
                    GlobalScope.launch(MainUIDispatcher) {
                        loadingDialog?.close(0)
                    }
                }

                override fun onFailure(p0: GradleConnectionException?) {
                    println("QuicklyRunGradleTask onFailure $p0")
                    p0?.printStackTrace()
                    GlobalScope.launch(MainUIDispatcher) {
                        loadingDialog?.close(0)
                        val innerException = p0?.cause?.cause?.cause
                        if (innerException != null)
                            LoadingDialog(
                                e,
                                innerException.stackTraceToString(),
                                "Gradle Task failed to run",
                                true
                            ).show()
                    }
                }
            })
        }
    }
}

