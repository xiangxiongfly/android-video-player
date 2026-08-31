package com.example.myapplication.list

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityVideoListBinding
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import com.shuyu.gsyvideoplayer.video.ListGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView

class VideoListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVideoListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rv_video)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val adapter = VideoAdapter(_root_ide_package_.com.example.myapplication.VideoData.getList())
        binding.rvVideo.adapter = adapter

        // 初次进入，默认第一个视频自动播放
        binding.rvVideo.doOnLayout {
            autoPlayFirstVisible()
        }

        // 滚动监听
        binding.rvVideo.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    autoPlayCenterVisible()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val first = layoutManager.findFirstVisibleItemPosition()
                val last = layoutManager.findLastVisibleItemPosition()
                val playPosition = GSYVideoManager.instance().playPosition
                if (playPosition >= 0 && (playPosition !in first..last)) {
                    if (!GSYVideoManager.isFullState(this@VideoListActivity)) {
                        GSYVideoManager.releaseAllVideos()
                    }
                }
            }
        })
    }

    /**
     * 自动播放第一个视频
     */
    private fun autoPlayFirstVisible() {
        val layoutManager = binding.rvVideo.layoutManager ?: return
        val childCount = layoutManager.childCount
        for (i in 0 until childCount) {
            val child = layoutManager.getChildAt(i) ?: continue
            val player = child.findViewById<ListGSYVideoPlayer>(R.id.video_player) ?: continue
            // 检查播放器是否已经添加
            if (!player.isShown || !player.isAttachedToWindow) continue
            val position = layoutManager.getPosition(child)
            val state = player.currentState
            // 只播放处于正常或错误状态的播放器
            if (state == GSYVideoView.CURRENT_STATE_NORMAL || state == GSYVideoView.CURRENT_STATE_ERROR) {
                executePlay(player, position)
                break
            }
        }
    }

    /**
     * 自动播放中间区域视频
     */
    private fun autoPlayCenterVisible() {
        val layoutManager = binding.rvVideo.layoutManager as LinearLayoutManager
        val childCount = layoutManager.childCount
        for (i in 0 until childCount) {
            val child = layoutManager.getChildAt(i) ?: continue
            val player = child.findViewById<ListGSYVideoPlayer>(R.id.video_player) ?: continue
            if (!isInCenterRange(player)) continue
            val position = layoutManager.getPosition(child)
            val state = player.currentState
            if (state == GSYVideoView.CURRENT_STATE_NORMAL || state == GSYVideoView.CURRENT_STATE_ERROR) {
                executePlay(player, position)
                break
            }
        }
    }

    private fun isInCenterRange(player: ListGSYVideoPlayer): Boolean {
        if (player.height <= 0) return false
        val location = IntArray(2)
        player.getLocationOnScreen(location)
        val halfHeight = player.height / 2
        val playerCenter = location[1] + halfHeight
        val screenCenter = CommonUtil.getScreenHeight(this) / 2
        val rangeTop = screenCenter - CommonUtil.dip2px(this, 180F)
        val rangeBottom = screenCenter + CommonUtil.dip2px(this, 180F)
        return playerCenter in rangeTop..rangeBottom
    }

    private fun executePlay(player: ListGSYVideoPlayer, position: Int) {
        val playPosition = GSYVideoManager.instance().playPosition
        if (playPosition >= 0 && playPosition != position) {
            GSYVideoManager.instance().releaseMediaPlayer()
        }
        player.startPlayLogic()
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }

    override fun onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) return
        super.onBackPressed()
    }
}