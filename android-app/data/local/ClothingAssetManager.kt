package com.virtualtrialroom.app.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClothingAssetManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    fun getOrCreateClothingUri(clothingId: String): String {
        val safeId = clothingId.replace(Regex("[^a-zA-Z0-9_-]"), "_")
        val directory = File(context.filesDir, CLOTHING_DIRECTORY).apply {
            mkdirs()
        }
        val file = File(directory, "$safeId.png")
        if (!file.exists()) {
            createGarmentBitmap(clothingId).compress(Bitmap.CompressFormat.PNG, 100, file.outputStream())
        }
        return Uri.fromFile(file).toString()
    }

    private fun createGarmentBitmap(clothingId: String): Bitmap {
        val bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = colorFor(clothingId)
            style = Paint.Style.FILL
        }
        val shirt = Path().apply {
            moveTo(150f, 92f)
            lineTo(362f, 92f)
            lineTo(462f, 216f)
            lineTo(390f, 264f)
            lineTo(362f, 438f)
            lineTo(150f, 438f)
            lineTo(122f, 264f)
            lineTo(50f, 216f)
            close()
        }
        canvas.drawPath(shirt, paint)
        paint.color = Color.argb(245, 245, 248, 255)
        canvas.drawRoundRect(205f, 92f, 307f, 162f, 28f, 28f, paint)
        return bitmap
    }

    private fun colorFor(clothingId: String): Int {
        return when (clothingId.lowercase()) {
            "dresses" -> Color.rgb(185, 28, 90)
            "outerwear" -> Color.rgb(22, 101, 52)
            else -> Color.rgb(37, 99, 235)
        }
    }

    private companion object {
        const val CLOTHING_DIRECTORY = "clothing_assets"
    }
}
