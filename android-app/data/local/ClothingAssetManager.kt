package com.virtualtrialroom.app.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.net.Uri
import android.webkit.MimeTypeMap
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClothingAssetManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    @Throws(IOException::class)
    fun importClothingImage(sourceUri: Uri): String {
        val mimeType = context.contentResolver.getType(sourceUri)
        require(mimeType in SUPPORTED_IMAGE_MIME_TYPES) {
            "Select a JPG, PNG, or WEBP garment image."
        }
        val id = "$UPLOADED_PREFIX${UUID.randomUUID()}"
        val extension = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(mimeType)
            ?: DEFAULT_IMAGE_EXTENSION
        val directory = ensureDirectory()
        val targetFile = File(directory, "$id.$extension")

        context.contentResolver.openInputStream(sourceUri).use { inputStream ->
            requireNotNull(inputStream) {
                "Unable to open selected garment image."
            }
            targetFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        require(targetFile.length() in 1..MAX_IMAGE_BYTES) {
            "Garment image must be smaller than 12 MB."
        }
        return id
    }

    fun getOrCreateClothingUri(clothingId: String): String {
        if (clothingId.startsWith(UPLOADED_PREFIX)) {
            return Uri.fromFile(findUploadedClothingFile(clothingId)).toString()
        }
        val safeId = clothingId.replace(Regex("[^a-zA-Z0-9_-]"), "_")
        val directory = ensureDirectory()
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

    private fun findUploadedClothingFile(clothingId: String): File {
        val directory = ensureDirectory()
        return directory.listFiles()
            .orEmpty()
            .firstOrNull { file ->
                file.isFile && file.nameWithoutExtension == clothingId
            }
            ?: throw IOException("Uploaded garment image was not found. Please select it again.")
    }

    private fun ensureDirectory(): File {
        return File(context.filesDir, CLOTHING_DIRECTORY).apply {
            if (!exists() && !mkdirs()) {
                throw IOException("Unable to create garment storage directory.")
            }
        }
    }

    private companion object {
        const val CLOTHING_DIRECTORY = "clothing_assets"
        const val UPLOADED_PREFIX = "uploaded_"
        const val DEFAULT_IMAGE_EXTENSION = "jpg"
        const val MAX_IMAGE_BYTES = 12L * 1024L * 1024L
        val SUPPORTED_IMAGE_MIME_TYPES = setOf(
            "image/jpeg",
            "image/png",
            "image/webp"
        )
    }
}
