package com.kunal.vqms

import android.app.Application
import android.content.Context
import com.kunal.vqms.util.LocaleHelper


class MainApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(base!!,"en"))
    }
}