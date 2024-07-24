package com.example.democomponentsinandroid.lifecycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.democomponentsinandroid.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivitySecondBinding.inflate(layoutInflater)
    }
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        count = intent.getIntExtra("count", 0)

        binding.textView.text = "count from main activity $count"
    }
}