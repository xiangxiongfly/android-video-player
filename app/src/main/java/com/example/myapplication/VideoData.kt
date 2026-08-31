package com.example.myapplication

private const val VIDEO_URL =
    "https://sf1-cdn-tos.huoshanstatic.com/obj/media-fe/xgplayer_doc_video/mp4/xgplayer-demo-360p.mp4"
private const val IMAGE_URL = "https://picsum.photos/800/600"

object VideoData {
    fun getList() = List(60) { index ->
        _root_ide_package_.com.example.myapplication.list.VideoItem(
            "标题${index}",
            VIDEO_URL,
            IMAGE_URL
        )
    }
}