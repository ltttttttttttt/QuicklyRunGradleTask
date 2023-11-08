package com.lt.quicklyRunGradleTask.util

import com.google.gson.Gson
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

/**
 * creator: lt  2023/7/4  lt.dygzs@qq.com
 * effect :
 * warning:
 */

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T> String.jsonToAny(): T = Gson().fromJson<T>(this, typeOf<T>().javaType)

fun Any.toJson(): String = Gson().toJson(this)