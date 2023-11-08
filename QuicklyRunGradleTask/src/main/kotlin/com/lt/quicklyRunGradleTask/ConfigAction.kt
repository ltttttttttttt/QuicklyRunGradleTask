package com.lt.quicklyRunGradleTask

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.unit.dp
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.ui.UIUtil
import com.lt.quicklyRunGradleTask.config.Config
import com.lt.quicklyRunGradleTask.config.ConfigUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import javax.swing.JComponent


class ConfigAction : DumbAwareAction() {

    override fun actionPerformed(e: AnActionEvent) {
        //入口3:配置翻译1和翻译2的行为(翻译api,根据翻译结果生成文本到指定的位置,kv的一些规则)
        doAction(e)
    }

    private fun doAction(e: AnActionEvent) {
        ConfigDialog(e).show()
    }

    class ConfigDialog(private val e: AnActionEvent) : DialogWrapper(e.project) {
        private val scope = CoroutineScope(Dispatchers.Default)
        private var cmdString by mutableStateOf("")
        private var runShowLoading by mutableStateOf(true)


        init {
            title = "Config QuicklyRunGradleTask"
            init()

            val config = ConfigUtil.getConfig(e)
            cmdString = config?.cmdString ?: ""
            runShowLoading = config?.runShowLoading ?: true
        }

        override fun dispose() {
            super.dispose()
            scope.cancel()
        }

        override fun createCenterPanel(): JComponent {
            return ComposePanel().apply {
                setBounds(0, 0, 800, 600)
                setContent {
                    MaterialTheme(
                        if (UIUtil.isUnderDarcula()) darkColors() else lightColors()
                    ) {
                        Surface {
                            App()
                        }
                    }
                }
            }
        }

        @Composable
        fun App() {
            Column {
                Text("Quick Command Configuration (:app:build) :")
                Spacer(Modifier.height(4.dp))
                TextField(cmdString, ::cmdString::set)
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Show loading window when running commands:")
                    Checkbox(runShowLoading, ::runShowLoading::set)
                }
            }
        }

        override fun doOKAction() {
            super.doOKAction()
            ConfigUtil.saveConfig(e, Config(cmdString, runShowLoading))
        }
    }
}

