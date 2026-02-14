package com.geohunt.core.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

fun Context.bitmapDescriptorFromVector(
    @DrawableRes vectorResId: Int,
    tintColor: Int,
    sizeDp: Int = 48
): BitmapDescriptor {
    val drawable = ContextCompat.getDrawable(this, vectorResId)!!.mutate()
    DrawableCompat.setTint(drawable, tintColor)

    val sizePx = (sizeDp * resources.displayMetrics.density).toInt()
    drawable.setBounds(0, 0, sizePx, sizePx)

    val bitmap = createBitmap(sizePx, sizePx)
    val canvas = Canvas(bitmap)
    drawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}