
package com.example.lab3rina_lifecycle

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

// C3: ViewModel with SavedStateHandle (C4)
class CounterViewModel(private val state: SavedStateHandle) : ViewModel() {

    // D1: Save and restore count using SavedStateHandle
    var count: Int
        get() = state["count"] ?: 0
        set(value) {
            state["count"] = value
        }

    init {
        Log.d("LC_VM", "ViewModel created")
    }

    override fun onCleared() {
        Log.d("LC_VM", "ViewModel destroyed")
    }
}
