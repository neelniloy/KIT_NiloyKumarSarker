package com.niloythings.kittask.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import java.util.*

@BindingAdapter("app:setLogo")
fun setLogo(iv: ImageView, byteArray: ByteArray) {
    val bitmap = byteArrayToBitmap(byteArray)
    iv.setImageBitmap(bitmap)
}

fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}