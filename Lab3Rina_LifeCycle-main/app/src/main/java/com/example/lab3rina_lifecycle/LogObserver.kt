
package com.example.lab3rina_lifecycle

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

// E1: Create Lifecycle Observer
class LogObserver : DefaultLifecycleObserver {

    // Log when activity starts
    override fun onStart(owner: LifecycleOwner) {
        Log.d("LC_OBS", "start")
    }

    // Log when activity stops
    override fun onStop(owner: LifecycleOwner) {
        Log.d("LC_OBS", "stop")
    }
}
