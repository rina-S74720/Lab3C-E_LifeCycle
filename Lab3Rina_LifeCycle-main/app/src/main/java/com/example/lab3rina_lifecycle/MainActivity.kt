
package com.example.lab3rina_lifecycle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : LoggingActivity() {

    // C3: Create ViewModel
    // D1: ViewModel uses SavedStateHandle
    private val vm: CounterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        val tvCount = findViewById<TextView>(R.id.tvCount)

        // C2: Restore count using savedInstanceState
        // Kept as a comment for comparison

        // C3 + D1: Display count from ViewModel and SavedStateHandle
        tvCount.text = "Count: ${vm.count}"

        // D2: Log stored value from SharedPreferences
        Log.d(
            "LC_PREF",
            "saved=${getSharedPreferences("lab", MODE_PRIVATE).getInt("saved", -1)}"
        )

        // E1: Register Lifecycle Observer
        lifecycle.addObserver(LogObserver())

        val btnPlus = findViewById<Button>(R.id.btnPlus)
        val btnDialog = findViewById<Button>(R.id.btnDialog)
        val btnSecScreen = findViewById<Button>(R.id.btnSecScreen)
        val btnTransparentActivity = findViewById<Button>(R.id.btnTransparent)
        val btnClose = findViewById<Button>(R.id.btnClose)

        btnPlus.setOnClickListener {
            // C3 + D1: Increase count using ViewModel
            vm.count++
            tvCount.text = "Count: ${vm.count}"
        }

        btnDialog.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Dialog")
                .setMessage("See Logcat")
                .setPositiveButton("OK", null)
                .show()
        }

        btnSecScreen.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        btnTransparentActivity.setOnClickListener {
            val intent = Intent(this, TransparentActivity::class.java)
            startActivity(intent)
        }

        btnClose.setOnClickListener {
            finish()
        }

        // E2: Collect Flow without lifecycle awareness
        lifecycleScope.launch {
            ticker().collect {
                Log.d("LC_FLOW", "tick $it")
            }
        }

        // E3: Collect Flow only when activity is STARTED
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                ticker().collect {
                    Log.d("LC_FLOW", "tick $it")
                }
            }
        }
    }

    // C2: Save count using onSaveInstanceState
    /*
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("count", vm.count)
    }
    */

    // D2: Save count using SharedPreferences when activity stops
    override fun onStop() {
        super.onStop()

        getSharedPreferences("lab", MODE_PRIVATE)
            .edit()
            .putInt("saved", vm.count)
            .apply()
    }
}

// E2 + E3: Create a simple ticker Flow
fun ticker() = flow {
    var number = 1

    while (true) {
        emit(number)
        number++
        delay(1000)
    }
}
