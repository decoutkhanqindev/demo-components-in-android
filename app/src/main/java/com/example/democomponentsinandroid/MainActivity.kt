package com.example.democomponentsinandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        logLifeCycle("onCreate")

        textView = findViewById(R.id.textView)
        textView.setOnClickListener {
//            startActivity(Intent(this, DialogActivity::class.java))
            startActivity(Intent(this, SecondActivity::class.java))
            finish()

//            // phai la 1 activity dialog thi moi co the che di 1 activity khac -> k the roi vao callback onPause
//            AlertDialog.Builder(this)
//                .setTitle("Demo")
//                .setMessage("Demo")
//                .setPositiveButton("OK") {_, _ -> }
//                .setNegativeButton("Cancel") {_, _ ->}
//                .show()
        }
    }

    override fun onStart() {
        super.onStart()
        logLifeCycle("onStart")
    }

    override fun onResume() {
        super.onResume()
        logLifeCycle("onResume")
    }

    override fun onPause() {
        super.onPause()
        logLifeCycle("onPause")
    }

    override fun onStop() {
        super.onStop()
        logLifeCycle("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        logLifeCycle("onDestroy")
    }

    private fun logLifeCycle(message: String) {
        Log.d(TAG, message)
    }

    companion object {
        const val TAG = "MainActivity"
    }
}