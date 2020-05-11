package com.mackosoft.lebonalbum

import android.app.Application
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.distribute.Distribute

class LeBonAlbumApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // init App Center integration
        if (BuildConfig.USES_APP_CENTER) {
            AppCenter.start(
                this,
                getString(R.string.app_center_app_secret),
                Analytics::class.java,
                Crashes::class.java,
                Distribute::class.java
            )
        }
    }

}