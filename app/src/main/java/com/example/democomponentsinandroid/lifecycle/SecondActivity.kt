package com.example.democomponentsinandroid.lifecycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.democomponentsinandroid.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivitySecondBinding.inflate(layoutInflater)
    }
    private var count = 0
    private lateinit var person: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        count = intent.getIntExtra("count", 0)
        person = intent.getParcelableExtra<Person>("person") as Person
//        binding.textView.text = "count from main activity $count"
        binding.textView.text = "person from main activity $person"
    }
}