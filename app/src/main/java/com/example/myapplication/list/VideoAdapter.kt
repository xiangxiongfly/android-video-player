package com.example.myapplication.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemVideoBinding
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder

class VideoAdapter(private val items: List<VideoItem>) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            binding.tvTitle.text = item.title
            val thumb = ImageView(itemView.context)
            Glide.with(itemView.context).load(item.coverUrl).into(thumb)
            GSYVideoOptionBuilder()
                .setUrl(item.url)
                .setVideoTitle(item.title)
                .setThumbImageView(thumb)
                .setPlayTag(position.toString())
                .setPlayPosition(position)
                .setCacheWithPlay(true)
                .setLooping(true)
                .build(binding.videoPlayer)
        }
    }

    override fun getItemCount() = items.size

    class ViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root)
}