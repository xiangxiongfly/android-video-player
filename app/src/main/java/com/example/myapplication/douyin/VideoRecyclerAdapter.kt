package com.example.myapplication.douyin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemVideoRecyclerBinding
import com.example.myapplication.list.VideoItem
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder

class VideoRecyclerAdapter(private val items: List<VideoItem>) :
    RecyclerView.Adapter<VideoRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemVideoRecyclerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = items[position]
        holder.apply {
            val thumb = ImageView(itemView.context)
            Glide.with(itemView).load(item.coverUrl).into(thumb)

            GSYVideoOptionBuilder()
                .setUrl(item.url)
                .setVideoTitle(item.title)
                .setThumbImageView(thumb)
                .setPlayTag("${position}")
                .setPlayPosition(position)
                .setCacheWithPlay(true)
                .setLooping(true)
                .setRotateViewAuto(true)
                .setLockLand(true)
                .setShowFullAnimation(true)
                .setNeedLockFull(true)
                .build(binding.videoPlayer)

            binding.videoPlayer.backButton.visibility = View.GONE
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        try {
            holder.binding.videoPlayer.release()
        } catch (e: Exception) {
        }
    }

    override fun getItemCount() = items.size

    class ViewHolder(val binding: ItemVideoRecyclerBinding) : RecyclerView.ViewHolder(binding.root)
}