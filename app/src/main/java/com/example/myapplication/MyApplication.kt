package com.example.myapplication

import android.app.Application
import com.shuyu.gsyvideoplayer.cache.CacheFactory
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initVideo()
    }

    private fun initVideo() {
        // 设置内核
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
        // 设置代理缓存模式
        CacheFactory.setCacheManager(ExoPlayerCacheManager::class.java)
    }
}