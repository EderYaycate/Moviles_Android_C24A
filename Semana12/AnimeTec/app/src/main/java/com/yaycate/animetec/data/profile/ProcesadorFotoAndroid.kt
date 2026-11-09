package com.yaycate.animetec.data.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ProcesadorFotoAndroid(
    private val context: Context
) : ProcesadorFoto {

    override suspend fun aBase64(uri: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val parsedUri = Uri.parse(uri)
            
            val matrix = Matrix()
            try {
                context.contentResolver.openInputStream(parsedUri)?.use { inputStream ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        val exif = ExifInterface(inputStream)
                        val orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL
                        )
                        when (orientation) {
                            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
                            ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
                        }
                    }
                }
            } catch (e: Exception) {
                // Si falla EXIF, continuar sin rotación
            }

            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            context.contentResolver.openInputStream(parsedUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, options)
            }

            var inSampleSize = 1
            val reqWidth = 512
            val reqHeight = 512
            if (options.outHeight > reqHeight || options.outWidth > reqWidth) {
                val halfHeight = options.outHeight / 2
                val halfWidth = options.outWidth / 2
                while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2
                }
            }

            val decodeOptions = BitmapFactory.Options().apply {
                this.inSampleSize = inSampleSize
            }
            
            val rawBitmap = context.contentResolver.openInputStream(parsedUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, decodeOptions)
            } ?: return@withContext Result.failure(Exception("No se pudo leer la imagen"))

            val bitmap = Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.width, rawBitmap.height, matrix, true)
            if (bitmap != rawBitmap) {
                rawBitmap.recycle()
            }

            val width = bitmap.width
            val height = bitmap.height
            val dimension = minOf(width, height)
            val x = (width - dimension) / 2
            val y = (height - dimension) / 2
            val squareBitmap = Bitmap.createBitmap(bitmap, x, y, dimension, dimension)
            if (squareBitmap != bitmap) {
                bitmap.recycle()
            }

            val scaledBitmap = Bitmap.createScaledBitmap(squareBitmap, 256, 256, true)
            if (scaledBitmap != squareBitmap) {
                squareBitmap.recycle()
            }

            val outputStream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            scaledBitmap.recycle()

            val bytes = outputStream.toByteArray()
            val base64String = Base64.encodeToString(bytes, Base64.NO_WRAP)
            Result.success(base64String)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
