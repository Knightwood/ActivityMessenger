package com.kelp.android.fms.runtime2.core

interface NavHost {
    fun getNavController(tag: String): NavController
}