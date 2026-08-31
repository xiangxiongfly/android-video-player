package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.douyin.DouyinActivity
import com.example.myapplication.list.VideoListActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnVideoList.setOnClickListener {
            startActivity(Intent(this, VideoListActivity::class.java))
        }
        binding.btnDouyin.setOnClickListener {
            startActivity(Intent(this, DouyinActivity::class.java))
        }
    }
}