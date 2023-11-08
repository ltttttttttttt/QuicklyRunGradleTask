package com.lt.quicklyRunGradleTask.config

data class Config(
    val cmdString: String,//命令内容
    var runShowLoading: Boolean,//运行命令时是否显示加载窗
)