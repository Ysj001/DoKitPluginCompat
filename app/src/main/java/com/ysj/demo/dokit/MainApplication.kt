package com.ysj.demo.dokit

import android.app.Application
import com.didichuxing.doraemonkit.DoKit

/**
 *
 *
 * @author Ysj
 * Create time: 2023/12/19
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DoKit.Builder(this).build()
    }

}