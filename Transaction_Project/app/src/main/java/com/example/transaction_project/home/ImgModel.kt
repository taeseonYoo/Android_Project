package com.example.transaction_project.home

import android.annotation.SuppressLint
import android.net.Uri

class ImgModel {
    private var imageUrl: String = ""

    @SuppressLint("NotConstructor")
    fun ImgModel (Url: String){
        imageUrl = Url
    }

    fun getUrl(): String {
        return imageUrl
    }

    fun setUrl(Url: String) {
        imageUrl = Url
    }


}