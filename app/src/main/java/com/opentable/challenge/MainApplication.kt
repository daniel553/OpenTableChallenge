package com.opentable.challenge

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 💡Needed for hilt di initialization
 */
@HiltAndroidApp
class MainApplication : Application()