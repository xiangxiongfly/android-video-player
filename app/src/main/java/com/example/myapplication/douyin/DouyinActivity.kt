package com.example.myapplication.douyin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.VideoData
import com.example.myapplication.databinding.ActivityDouyinBinding
import com.shuyu.gsyvideoplayer.GSYVideoManager

class DouyinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDouyinBinding
    private lateinit var adapter: VideoRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDouyinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = VideoRecyclerAdapter(VideoData.getList())
        binding.viewPager2.adapter = adapter

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val playPosition = GSYVideoManager.instance().playPosition
                if (playPosition >= 0 && playPosition != position) {
                    GSYVideoManager.releaseAllVideos()
                }
                playVideo(position)
            }
        })

        binding.viewPager2.post {
            playVideo(0)
        }
    }

    private fun playVideo(position: Int) {
        if (position !in 0 until adapter.itemCount - 1) {
            return
        }

        binding.viewPager2.postDelayed({
            val recyclerView = binding.viewPager2.getChildAt(0) as RecyclerView
            val holder = recyclerView.findViewHolderForAdapterPosition(position) as? VideoRecyclerAdapter.ViewHolder
            holder?.let {
                it.binding.videoPlayer.startPlayLogic()
            }
        }, 50)
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume(false)
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }
}