package com.virtualtrialroom.app.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.webkit.MimeTypeMap
import android.net.Uri
import com.virtualtrialroom.app.domain.model.ImageSource
import com.virtualtrialroom.app.domain.model.UserPhoto
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import java.time.Clock
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoFileManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val clock: Clock
) {
    fun createCaptureFile(): File {
        val directory = ensureDirectory(CAPTURE_DIRECTORY)
        val timestamp = DateTimeFormatter.ISO_INSTANT.format(clock.instant())
            .replace(":", "-")
        return File(directory, "try_on_$timestamp.jpg")
    }

    @Throws(IOException::class)
    fun importGalleryImage(sourceUri: Uri): UserPhoto {
        val mimeType = context.contentResolver.getType(sourceUri)
        require(isSupportedImageMimeType(mimeType)) {
            "Select a JPG, PNG, or WEBP image."
        }
        val directory = ensureDirectory(IMPORT_DIRECTORY)
        val extension = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(mimeType)
            ?: DEFAULT_IMAGE_EXTENSION
        val targetFile = File(directory, "import_${clock.millis()}.$extension")

        context.contentResolver.openInputStream(sourceUri).use { inputStream ->
            requireNotNull(inputStream) {
                "Unable to open selected image."
            }
            targetFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        validateImageFile(targetFile)
        return toUserPhoto(
            file = targetFile,
            source = ImageSource.GALLERY
        )
    }

    fun toUserPhoto(file: File, source: ImageSource): UserPhoto {
        if (source == ImageSource.CAMERA) {
            validateReadableImageFile(file)
            normalizeCameraImage(file)
        } else {
            validateImageFile(file)
        }
        val dimensions = readImageDimensions(file)
        return UserPhoto(
            id = UUID.nameUUIDFromBytes(file.absolutePath.toByteArray()).toString(),
            localUri = Uri.fromFile(file).toString(),
            source = source,
            width = dimensions.width,
            height = dimensions.height,
            capturedAtEpochMillis = clock.millis()
        )
    }

    fun findPhotoById(photoId: String): UserPhoto? {
        return listOf(
            ensureDirectory(CAPTURE_DIRECTORY) to ImageSource.CAMERA,
            ensureDirectory(IMPORT_DIRECTORY) to ImageSource.GALLERY
        ).asSequence()
            .flatMap { (directory, source) ->
                directory.listFiles()
                    .orEmpty()
                    .asSequence()
                    .filter { it.isFile }
                    .map { file -> toUserPhoto(file, source) }
            }
            .firstOrNull { it.id == photoId }
    }

    private fun readImageDimensions(file: File): ImageDimensions {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(file.absolutePath, options)
        return ImageDimensions(
            width = options.outWidth.coerceAtLeast(0),
            height = options.outHeight.coerceAtLeast(0)
        )
    }

    private fun validateImageFile(file: File) {
        val dimensions = validateReadableImageFile(file)
        require(file.length() in 1..MAX_IMAGE_BYTES) {
            "Image must be smaller than 12 MB."
        }
    }

    private fun validateReadableImageFile(file: File): ImageDimensions {
        val dimensions = readImageDimensions(file)
        require(file.length() > 0L) {
            "Image file is empty."
        }
        require(dimensions.width > 0 && dimensions.height > 0) {
            "Selected file is not a readable image."
        }
        return dimensions
    }

    private fun normalizeCameraImage(file: File) {
        val dimensions = readImageDimensions(file)
        if (dimensions.width <= MAX_CAMERA_EDGE && dimensions.height <= MAX_CAMERA_EDGE) {
            return
        }

        val sampleSize = calculateSampleSize(dimensions.width, dimensions.height, MAX_CAMERA_EDGE)
        val bitmap = BitmapFactory.decodeFile(
            file.absolutePath,
            BitmapFactory.Options().apply { inSampleSize = sampleSize }
        ) ?: throw IOException("Captured image could not be processed.")

        file.outputStream().use { outputStream ->
            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, outputStream)) {
                throw IOException("Captured image could not be saved.")
            }
        }
        bitmap.recycle()
        validateImageFile(file)
    }

    private fun calculateSampleSize(width: Int, height: Int, maxEdge: Int): Int {
        var sampleSize = 1
        var sampledWidth = width
        var sampledHeight = height
        while (sampledWidth > maxEdge || sampledHeight > maxEdge) {
            sampleSize *= 2
            sampledWidth = width / sampleSize
            sampledHeight = height / sampleSize
        }
        return sampleSize.coerceAtLeast(1)
    }

    private fun isSupportedImageMimeType(mimeType: String?): Boolean {
        return mimeType in SUPPORTED_IMAGE_MIME_TYPES
    }

    private fun ensureDirectory(name: String): File {
        return File(context.filesDir, name).apply {
            if (!exists() && !mkdirs()) {
                throw IOException("Unable to create image storage directory.")
            }
        }
    }

    private data class ImageDimensions(
        val width: Int,
        val height: Int
    )

    private companion object {
        const val CAPTURE_DIRECTORY = "captured_photos"
        const val IMPORT_DIRECTORY = "imported_photos"
        const val DEFAULT_IMAGE_EXTENSION = "jpg"
        const val MAX_IMAGE_BYTES = 12L * 1024L * 1024L
        const val MAX_CAMERA_EDGE = 1600
        const val JPEG_QUALITY = 88
        val SUPPORTED_IMAGE_MIME_TYPES = setOf(
            "image/jpeg",
            "image/png",
            "image/webp"
        )
    }
}
