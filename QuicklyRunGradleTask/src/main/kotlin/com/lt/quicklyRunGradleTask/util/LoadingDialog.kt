package com.lt.quicklyRunGradleTask.util

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.ui.UIUtil
import javax.swing.JComponent

/**
 * creator: lt  2023/7/4  lt.dygzs@qq.com
 * effect :
 * warning:
 */
class LoadingDialog(
    val e: AnActionEvent,
    val msg: String,
    title: String = "QuicklyRunGradleTask Loading",
    val isCanSelect: Boolean = false,
) :
    DialogWrapper(e.project) {
    init {
        this.title = title
        init()
    }

    override fun createCenterPanel(): JComponent {
        return ComposePanel().apply {
            setBounds(0, 0, 800, 600)
            setContent {
                MaterialTheme(if (UIUtil.isUnderDarcula()) darkColors() else lightColors()) {
                    Surface {
                        Column(Modifier.verticalScroll(rememberScrollState())) {
                            if (isCanSelect)
                                SelectionContainer {
                                    Text(msg)
                                }
                            else
                                Text(msg)
                        }
                    }
                }
            }
        }
    }
}