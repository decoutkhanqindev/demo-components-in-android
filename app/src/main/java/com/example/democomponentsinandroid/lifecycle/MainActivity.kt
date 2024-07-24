package com.example.democomponentsinandroid.lifecycle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.democomponentsinandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        logLifeCycle("onCreate")

//        binding.textView.setOnClickListener {
////            startActivity(Intent(this, DialogActivity::class.java))
//            startActivity(Intent(this, SecondActivity::class.java))
//
////            // phai la 1 activity dialog thi moi co the che di 1 activity khac -> k the roi vao callback onPause
////            AlertDialog.Builder(this)
////                .setTitle("Demo")
////                .setMessage("Demo")
////                .setPositiveButton("OK") {_, _ -> }
////                .setNegativeButton("Cancel") {_, _ ->}
////                .show()
//        }

        binding.textView.text = "click $count times"

        if (savedInstanceState != null) {
            count = savedInstanceState.getInt("count")
        }

        binding.btn.setOnClickListener {
            count++
            binding.textView.text = "click $count times"
        }

        binding.btn1.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java).apply {
                putExtra("count", count)
                putExtra("person", Person("1", "Khang"))
            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("count", count)
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