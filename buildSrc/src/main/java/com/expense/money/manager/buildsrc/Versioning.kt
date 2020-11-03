package com.expense.money.manager.buildsrc

object Versioning {
    const val major = 1
    const val minor = 0
    const val path = 2

    @JvmStatic
    fun generateVersionName() = "$major.$minor.$path"
}