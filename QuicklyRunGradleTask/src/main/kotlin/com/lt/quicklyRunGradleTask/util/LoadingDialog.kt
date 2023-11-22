package com.lt.quicklyRunGradleTask.util

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
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
            setBounds(0, 0, 1200, 600)
            setContent {
                MaterialTheme(if (UIUtil.isUnderDarcula()) darkColors() else lightColors()) {
                    Surface {
                        if (isCanSelect) {
                            Box(Modifier.fillMaxSize()) {
                                val scrollState = rememberScrollState()
                                Column(Modifier.verticalScroll(scrollState)) {
                                    LongText()
                                }
                                VerticalScrollbar(
                                    rememberScrollbarAdapter(scrollState),
                                    Modifier.align(Alignment.CenterEnd).fillMaxHeight().width(5.dp)
                                )
                            }
                        } else
                            Text(msg)
                    }
                }
            }
        }
    }

    @Composable
    private fun LongText() {
        val redColor = MaterialTheme.colors.error
        val blueColor = MaterialTheme.colors.primary
        val text = remember {
            buildAnnotatedString {
                msg.split("\n")
                    .forEach {
                        if (it.startsWith("\t")) {
                            //方法栈信息
                            val index = it.indexOf("(")
                            if (index < 0) {
                                append(it)
                                append("\n")
                                return@forEach
                            }
                            append(it.substring(0, index))
                            withStyle(SpanStyle(color = blueColor)) {
                                append(it.substring(index, it.length))
                            }
                        } else {
                            //错误提示信息
                            withStyle(SpanStyle(color = redColor)) {
                                append(it)
                            }
                        }
                        append("\n")
                    }
            }
        }
        SelectionContainer {
            Text(text)
        }
    }
}